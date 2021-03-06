package com.beyond_tech.seyanah_admin.fragments

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.beyond_tech.seyanah_admin.R
import com.beyond_tech.seyanah_admin.adapters.rv.RecyclerNotificationAdapter
import com.beyond_tech.seyanah_admin.constants.Constants
import com.beyond_tech.seyanah_admin.events.RxEvent
import com.beyond_tech.seyanah_admin.fire_utils.RefBase
import com.beyond_tech.seyanah_admin.interfaces.OnNotificationClicked
import com.beyond_tech.seyanah_admin.models.Category
import com.beyond_tech.seyanah_admin.models.Notification
import com.beyond_tech.seyanah_admin.models.OrderRequest
import com.beyond_tech.seyanah_admin.models.User
import com.beyond_tech.seyanah_admin.rxbus.RxBus
import com.developer.kalert.KAlertDialog
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    OnNotificationClicked {


    var tvNoNotificationsYet: TextView? = null
    var detached: Boolean = false
    val TAG = NotificationsFragment::class.java.name
    var listOfNotification: ArrayList<Notification> = ArrayList<Notification>()
    var listOfCPNotification: ArrayList<Notification> = ArrayList<Notification>()
    var listOfOrdersRequest: ArrayList<OrderRequest> = ArrayList<OrderRequest>()
    var listOfCategory: ArrayList<Category> = ArrayList<Category>()
    var title = ""
    var body = ""
    var progress: AlertDialog? = null
    var notiType = ""
    var adapterNotification: RecyclerNotificationAdapter? = null
    private var spruceAnimator: Animator? = null
    var linerLayoutManager: LinearLayoutManager? = null
    private val mHandler = Handler()
    var pos = 0
    var activity: Activity? = null
    //    var context: Context? = null
    private lateinit var disposable: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = getActivity()
//        context = getContext()

//        setHasOptionsMenu(true)
//        setMenuVisibility(true)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity!!.menuInflater.inflate(R.menu.menu_main, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.makeAllAsRead -> {
//                Toast.makeText(
//                    applicationContext, "Make All As Read",
//                    Toast.LENGTH_SHORT
//                ).show()
                changeAllCpNotificationAsRead()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun changeAllCpNotificationAsRead() {

        setHasOptionsMenu(false)
        setMenuVisibility(false)

        RefBase.cpNotification()
            .orderByChild(Constants.ORDER_STATE)
            .equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeEventListener(this)
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        dataSnapshot.children.iterator().forEach { dataSnapshot ->
                            dataSnapshot.ref.child(Constants.ORDER_STATE).setValue(false)
                        }

                        Toast.makeText(
                            activity, "All Notifications Maked As Read",
                            Toast.LENGTH_SHORT
                        ).show()

//                        setHasOptionsMenu(false)
//                        setMenuVisibility(false)

                    }
                }

                override fun onCancelled(p0: DatabaseError) {


                }
            })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        fetchTheNumberOfNotification()

    }

    var notificationCounter: Int = 0

    private fun fetchTheNumberOfNotification() {
        RefBase.cpNotification()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeEventListener(this)
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        notificationCounter = 0
                        for (snap in dataSnapshot.children) {
                            val hashMap =
                                snap.value as java.util.HashMap<String, Any?>?
                            if (hashMap != null) {
                                if (hashMap[Constants.ORDER_STATE] != null) {
                                    val shown =
                                        hashMap[Constants.ORDER_STATE] as Boolean
                                    if (shown) {
                                        notificationCounter++
                                    }
                                }
                            }
                        }
                        Log.e(TAG, notificationCounter.toString())

                        if (notificationCounter != 0) {

                            setHasOptionsMenu(true)
                            setMenuVisibility(true)

                        }

                    } else {

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(recycler_notifi)
        initVars()
//        loadNotification()
//        loadCPNotification()
//        initSpruce()
//        Log.e(TAG, "test1999")
        accessSwipeRefresh()

//        setupRecyclerView(shimmer_recycler_view)
//        accessShimmerRecycler()

        postDelayed()

        accessMutliStateToggleButton()


        disposable = RxBus.listen(RxEvent.EventShowMakeAllAsRead::class.java)
            .subscribe {
                setHasOptionsMenu(it.updated)
                setMenuVisibility(it.updated)

                Log.e(TAG, "publishing - Subscribing")


            }

    }

    private fun initVars() {
        tvNoNotificationsYet = view!!.findViewById(R.id.tvNoNotificationsYet)
    }

    private fun accessMutliStateToggleButton() {
        multiStateToggleButton.setOnValueChangedListener { position ->
            Log.e(
                TAG,
                "Position: $position"
            )

            // loadCPNotification(position)
            pos = position
            postDelayed()
        }
        multiStateToggleButton.setElements(R.array.titles_array, 0)


    }

    private fun accessSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(context!!, R.color.Orange),
            ContextCompat.getColor(context!!, R.color.Orange),
            ContextCompat.getColor(context!!, R.color.Orange)
        )

    }

    private fun setupRecyclerView(recycler_notifi: RecyclerView?) {
        adapterNotification = RecyclerNotificationAdapter(
            activity,
            listOfNotification,
            listOfOrdersRequest,
            listOfCategory,
            this
        )


        linerLayoutManager = LinearLayoutManager(activity)
        (linerLayoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
        recycler_notifi!!.layoutManager = linerLayoutManager
        recycler_notifi.setHasFixedSize(true)
        recycler_notifi.itemAnimator = DefaultItemAnimator()
        recycler_notifi.adapter = adapterNotification//set empty by default
        adapterNotification!!.notifyDataSetChanged()


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.e(TAG, "onSwiped")

                Log.e(TAG, "onSwiped" + viewHolder.adapterPosition)
                var notification = listOfNotification[viewHolder.adapterPosition]
                Log.e(TAG, "onSwiped" + notification.notiType)

                when (notification.notiType) {

                    Constants.REQUESTS -> {


                        val position: Int = viewHolder.adapterPosition
                        val notiItem = listOfNotification[position]


                        Log.e(
                            TAG,
                            "Swipe ya man From request" + viewHolder.adapterPosition + " and " + notiItem.requestId
                        )

                        RefBase.cpNotification()
                            .orderByChild(Constants.ORDER_ID)
                            .equalTo(notiItem.requestId)
                            .addChildEventListener(object : ChildEventListener {

                                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {

                                        Log.e(TAG, "Swipe to delete " + dataSnapshot.key.toString())

                                        RefBase.cpNotification().child(dataSnapshot.key.toString())
                                            .removeValue().addOnSuccessListener {
                                                listOfNotification.removeAt(position)
                                                adapterNotification = RecyclerNotificationAdapter(
                                                    activity,
                                                    listOfNotification,
                                                    listOfOrdersRequest,
                                                    listOfCategory,
                                                    this@NotificationsFragment
                                                )
                                                recycler_notifi.adapter = adapterNotification
                                            }

                                    } else {
                                        getActivity()?.let {
                                            Toasty.warning(
                                                it
                                                , getString(R.string.ios_error_cant_del_this_item)
                                            ).show()
                                        }

                                    }

                                }

                                override fun onChildChanged(p0: DataSnapshot, p1: String?) {


                                }

                                override fun onChildMoved(p0: DataSnapshot, p1: String?) {


                                }

                                override fun onChildRemoved(p0: DataSnapshot) {


                                }

                                override fun onCancelled(databaseError: DatabaseError) {


                                }


                            })
//                        RefBase.cpNotification()


                        adapterNotification = RecyclerNotificationAdapter(
                            activity,
                            listOfNotification,
                            listOfOrdersRequest,
                            listOfCategory,
                            this@NotificationsFragment
                        )
                        recycler_notifi.adapter = adapterNotification
//                        adapterNotification!!.notifyDataSetChanged()
                    }

                    Constants.USERS -> {

                        val position2: Int = viewHolder.adapterPosition
                        val notiItem2 = listOfNotification[position2]

                        Log.e(
                            TAG,
                            "Swipe ya man From USER " + notiItem2.requestId + " and " + notiItem2.customerId + " and " + notiItem2.date
                        )

                        RefBase.cpNotification()
                            .orderByChild(Constants.time)
                            .equalTo(notiItem2.date)
                            .addChildEventListener(object : ChildEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {

                                        Log.e(
                                            TAG,
                                            "Swipe to delete user " + dataSnapshot.key.toString()
                                        )

                                        RefBase.cpNotification().child(dataSnapshot.key.toString())
                                            .removeValue().addOnSuccessListener {
                                                listOfNotification.removeAt(position2)
                                                adapterNotification = RecyclerNotificationAdapter(
                                                    activity,
                                                    listOfNotification,
                                                    listOfOrdersRequest,
                                                    listOfCategory,
                                                    this@NotificationsFragment
                                                )
                                                recycler_notifi.adapter = adapterNotification
                                            }

                                    }

                                }

                                override fun onChildRemoved(p0: DataSnapshot) {
                                    Log.e(TAG, "remove a User ")

                                }

                            })


                        adapterNotification = RecyclerNotificationAdapter(
                            activity,
                            listOfNotification,
                            listOfOrdersRequest,
                            listOfCategory,
                            this@NotificationsFragment
                        )
                        recycler_notifi.adapter = adapterNotification
//                        adapterNotification!!.notifyDataSetChanged()


                    }

                    Constants.WORKERS -> {
                        Log.e(TAG, "Swip ya man From Worker ")


                        val position3: Int = viewHolder.adapterPosition
                        val notiItem3 = listOfNotification[position3]

                        Log.e(
                            TAG,
                            "Swipe ya man From Worker " + notiItem3.requestId + " and " + notiItem3.freelancerId + " and " + notiItem3.date
                        )

                        RefBase.cpNotification()
                            .orderByChild(Constants.time)
                            .equalTo(notiItem3.date)
                            .addChildEventListener(object : ChildEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {

                                        Log.e(
                                            TAG,
                                            "Swipe to delete user " + dataSnapshot.key.toString()
                                        )

                                        RefBase.cpNotification().child(dataSnapshot.key.toString())
                                            .removeValue().addOnSuccessListener {
                                                listOfNotification.removeAt(position3)
                                                adapterNotification = RecyclerNotificationAdapter(
                                                    activity,
                                                    listOfNotification,
                                                    listOfOrdersRequest,
                                                    listOfCategory,
                                                    this@NotificationsFragment
                                                )
                                                recycler_notifi.adapter = adapterNotification
                                            }

//                                        zoooooz
                                    }

                                }

                                override fun onChildRemoved(p0: DataSnapshot) {

                                    Log.e(TAG, "remove a worker ")


                                }

                            })

                        adapterNotification = RecyclerNotificationAdapter(
                            activity,
                            listOfNotification,
                            listOfOrdersRequest,
                            listOfCategory,
                            this@NotificationsFragment
                        )
                        recycler_notifi.adapter = adapterNotification
//                        adapterNotification!!.notifyDataSetChanged()

                    }

                }

            }

        }

//        ItemTouchHelper heleper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
//
//        }

        val item = ItemTouchHelper(itemTouchHelperCallback)
        item.attachToRecyclerView(recycler_notifi)

    }

    override fun onDetach() {
        super.onDetach()

        detached = true
        if (progress != null) {
            progress?.dismiss()
        }
    }

    private fun loadCPNotification(position: Int) {

        listOfCPNotification.clear()


//        val progress = Helper(activity).createProgressDialog(getString(R.string.please_wait))
//        progress?.show()

        var keyword = ""
        when (position) {
            0 -> {
                keyword = Constants.USERS
                Log.e(TAG, Constants.USERS)
            }
            1 -> {
                keyword = Constants.WORKERS
                Log.e(TAG, Constants.WORKERS)

            }
            2 -> {
                keyword = Constants.REQUESTS
                Log.e(TAG, Constants.REQUESTS)


            }
//            3 -> {
//                keyword = Constants.OFFER
//
//
//            }
        }

        RefBase.cpNotification()
            .orderByChild(Constants.KEYWORD)
            .equalTo(keyword)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeEventListener(this)
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {

                        Log.e(TAG, "tYPE TYPE$dataSnapshot")

                        dataSnapshot.children.iterator().forEach {
                            Log.e(TAG, "AFTER LOOP$it")

                            val map: HashMap<String, Object> =
                                it.value as HashMap<String, Object>
                            Log.e(TAG, "tYPE TYPEx$map")

                            val keyword = map[Constants.KEYWORD].toString()
                            val date = map[Constants.DATE].toString()

//                    var keyword = dataSnapshot.child(Constants.KEYWORD).value.toString()
                            Log.e(TAG, "tYPE TYPExx$keyword")

                            var notification: Notification = Notification()



                            when (keyword) {
                                Constants.REQUESTS -> {
                                    Log.e(TAG, "Requestx$keyword")
                                    title = activity!!.getString(R.string.new_order)
                                    body = activity!!.getString(R.string.new_order_requested)
                                    notification.customerId =
                                        map[Constants.CUSTOMER_ID].toString()
                                    if (map[Constants.ORDER_ID] != null) {
                                        notification.requestId = map[Constants.ORDER_ID].toString()
                                    } else {
                                        notification.requestId = ""
                                    }
                                }

                                //                        val vvvv = "Customer has been send the request successfully "
                                Constants.USERS -> {
                                    Log.e(TAG, "tEEEEEEEEEEEt")
                                    title = activity!!.getString(R.string.new_user)
                                    body = activity!!.getString(R.string.new_user_registered)
                                }

                                Constants.WORKERS -> {
                                    notification.freelancerId =
                                        map.get(Constants.FREE_LANCER_ID).toString()
                                    title = activity!!.getString(R.string.new_free)
                                    body = activity!!.getString(R.string.new_free_registered)


                                }
                            }


                            notification.title = title
                            notification.message = body
                            notification.notiType = keyword
                            notification.date = date

                            listOfNotification.add(notification)


                            Log.e(TAG, "size_8484 =  " + listOfCPNotification.size.toString())

                            Log.e(
                                TAG,
                                "Test5550 " + dataSnapshot.childrenCount + ", " + dataSnapshot.toString() + "\n"
                            )
                            Log.e(TAG, "Soso" + notification.requestId)


                        }

                        listOfNotification.reverse()


                        if (!detached) {
                            if (listOfNotification.isNotEmpty()) {
                                tvNoNotificationsYet!!.visibility = View.GONE
                                recycler_notifi.visibility = View.VISIBLE
                            } else {
                                tvNoNotificationsYet!!.visibility = View.VISIBLE
                                recycler_notifi.visibility = View.GONE
                            }

                            recycler_notifi.adapter = RecyclerNotificationAdapter(
                                activity,
                                listOfNotification,
                                listOfOrdersRequest,
                                listOfCategory,
                                this@NotificationsFragment
                            )

                            Log.e(
                                TAG,
                                "Test5550 " + dataSnapshot.childrenCount + ", " + dataSnapshot.toString() + "\n"
                            )

                        }

                    } else {
                        if (listOfNotification.isNotEmpty()) {
                            tvNoNotificationsYet!!.visibility = View.GONE
                            recycler_notifi.visibility = View.VISIBLE
                        } else {
                            tvNoNotificationsYet!!.visibility = View.VISIBLE
                            recycler_notifi.visibility = View.GONE
                        }
                    }
                    if (!detached) {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
    }

    override fun onRefresh() {
        postDelayed()
    }

    private fun postDelayed() {
        swipeRefreshLayout.isRefreshing = true
        listOfCPNotification.clear()
        listOfNotification.clear()
        loadCPNotification(pos)

    }

    override fun onNotificationClciked(notification: Notification, position: Int) {
        Log.e(TAG, "clicked ")

        when (notification.notiType) {
            Constants.REQUESTS -> {
                Log.e(TAG, "Hoba_Lala" + notification.notiType)
                Log.e(TAG, "Hoba_Lala" + notification.requestId)

                //show alert dialog details
                showAlertDialog(notification)


            }
            Constants.WORKERS -> {
                Log.e(TAG, notification.notiType)


            }
            Constants.USERS -> {
                Log.e(TAG, notification.notiType)


            }
            else -> {

            }
        }

    }

    @SuppressLint("InflateParams")
    private fun showAlertDialog(notification: Notification) {
        val v = LayoutInflater.from(activity)
            .inflate(R.layout.layout_preview_order_details, null)
//        val alertDialog: AlertDialog
//        val builder: AlertDialog.Builder
//        builder = AlertDialog.Builder(activity)
//            .setView(v)
//            .setPositiveButton(
//                getString(R.string.ok)
//            ) { dialog, _ -> dialog.dismiss() }
//        alertDialog = builder.create()
//        alertDialog.setOnShowListener {
//            fetchRequestDetails(v, notification)
//        }
//        if (!alertDialog.isShowing) {
//            alertDialog.show()
//        }


//        val alertDialog = KAlertDialog(activity, KAlertDialog.NORMAL_TYPE)
        val alertDialog = KAlertDialog(activity, KAlertDialog.SUCCESS_TYPE)
            .setCustomView(v)
            .setTitleText(getString(R.string.app_name_root))
//            .setContentText("Won't be able to recover this file!")
//            .setCancelText("No,cancel plx!")
            .setConfirmText(getString(R.string.dialog_ok))
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
//            .setOnShowListener {
//                fetchRequestDetails(v, notification)
//            }
//            .showCancelButton(true)
//            .setCancelClickListener {
//
//            }

        alertDialog.setOnShowListener {
            Log.e(TAG, "setOnShowListener")
            fetchRequestDetails(alertDialog, v, notification)
        }
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.show()

    }

    private fun fetchRequestDetails(
        dialog: AlertDialog,
        v: View,
        notification: Notification
    ) {
        val tvName = v.findViewById<TextView>(R.id.tvFullName)
        val tvPhone = v.findViewById<TextView>(R.id.tvPhone)
        val tvZone = v.findViewById<TextView>(R.id.tvZone)
        val tvOrderDetails = v.findViewById<TextView>(R.id.tvOrderDetails)

        Log.e(TAG, "customerId_34034 = " + notification.customerId)

        RefBase.refUser(notification.customerId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeEventListener(this)
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        Log.e(TAG, "onDataChange: Finished pro4 ")
                        val user = dataSnapshot.getValue(User::class.java)

                        if (user!!.userName.isNotEmpty()) {
                            tvName.text = user.userName
                        }
                        tvPhone.text = user.userPhoneNumber

                        RefBase.refRequest(notification.requestId)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    dataSnapshot.ref.removeEventListener(this)
                                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                                        Log.e(TAG, "onDataChange: Finished pro4 ")
                                        val orderRequest =
                                            dataSnapshot.getValue(OrderRequest::class.java)
                                        tvZone.text = orderRequest!!.location.country
                                        tvOrderDetails.text = orderRequest.orderDescription
                                    } else {
//                                        Log.e(TAG, "ShsosXXX")
//                                        Handler().postDelayed(Runnable {
//                                            getActivity()?.let {
//                                                Toasty.warning(
//                                                    it
//                                                    , getString(R.string.ios_error)
//                                                ).show()
//                                                dialog.dismiss()
//                                            }
//                                        }, 700)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
//

                    } else {
                        Log.e(TAG, "ShsosXXX")
                        Handler().postDelayed({
                            getActivity()?.let {
                                Toasty.warning(
                                    it
                                    , getString(R.string.ios_error)
                                ).show()
                                dialog.dismiss()
                            }
                        }, 700)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }


}