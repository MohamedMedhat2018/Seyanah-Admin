package com.beyond_tech.seyanah_admin.fragments

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.beyond_tech.seyanah_admin.fire_utils.RefBase
import com.beyond_tech.seyanah_admin.interfaces.OnNotificationClicked
import com.beyond_tech.seyanah_admin.models.Category
import com.beyond_tech.seyanah_admin.models.Notification
import com.beyond_tech.seyanah_admin.models.OrderRequest
import com.beyond_tech.seyanah_admin.models.User
import com.beyond_tech.seyanahadminapp.helper.Helper
import com.developer.kalert.KAlertDialog
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.willowtreeapps.spruce.Spruce
import com.willowtreeapps.spruce.animation.DefaultAnimations
import com.willowtreeapps.spruce.sort.DefaultSort
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
    var notification: Notification = Notification()
    var adapterNotification: RecyclerNotificationAdapter? = null
    private var spruceAnimator: Animator? = null
    var linerLayoutManager: LinearLayoutManager? = null
    private val mHandler = Handler()
    var pos = 0


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
// Resource id, position one is selected by default
        multiStateToggleButton.setElements(R.array.titles_array, 0)


//        val titles_array =
//            resources.getStringArray(R.array.titles_array)
//        val buttons = Array<View?>(3) { null }
//
//
////        val multiStateToggleButton =
////            this.findViewById(R.id.mstb_multi_id) as MultiStateToggleButton
//
//        for (i in 0..2) {
//            //println(i)
//            val button =
//                layoutInflater.inflate(R.layout.custom_button, multiStateToggleButton, false)
//            buttons[i] = button;
//
//        }
//        multiStateToggleButton.setButtons(buttons, BooleanArray(buttons.size))

    }

    private fun accessShimmerRecycler() {

        listOfNotification.add(Notification())
        listOfNotification.add(Notification())
        listOfNotification.add(Notification())
        listOfNotification.add(Notification())
        listOfNotification.add(Notification())
        listOfNotification.add(Notification())


        adapterNotification = RecyclerNotificationAdapter(
            activity,
            listOfNotification,
            listOfOrdersRequest,
            listOfCategory,
            this
        )



        linerLayoutManager = LinearLayoutManager(activity)
//        linerLayoutManager = object : LinearLayoutManager(context) {
//            override fun onLayoutChildren(
//                recycler: RecyclerView.Recycler?,
//                state: RecyclerView.State
//            ) {
//                super.onLayoutChildren(recycler, state)
//                initSpruce()
//            }
//        }
//        (linerLayoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
        shimmer_recycler_view.layoutManager = linerLayoutManager
        shimmer_recycler_view.showShimmerAdapter()
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
//        linerLayoutManager = object : LinearLayoutManager(context) {
//            override fun onLayoutChildren(
//                recycler: RecyclerView.Recycler?,
//                state: RecyclerView.State
//            ) {
//                super.onLayoutChildren(recycler, state)
//                initSpruce()
//            }
//        }
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

//                (viewHolder as adapterNotification)
//                adapterNotification

//                val position: Int = viewHolder.adapterPosition
//                val notiItem = listOfNotification[position]

                when (notification.notiType) {

                    Constants.REQUESTS -> {


                        val position: Int = viewHolder.adapterPosition
                        val notiItem = listOfNotification[position]


                        Log.e(TAG, "Swipe ya man From request" + viewHolder.adapterPosition + " and " + notiItem.requestId )

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

                        Log.e(TAG, "Swipe ya man From USER " + notiItem2.requestId + " and " + notiItem2.customerId + " and " + notiItem2.date)

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

                                        Log.e(TAG, "Swipe to delete user " + dataSnapshot.key.toString())

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

                        Log.e(TAG, "Swipe ya man From Worker " + notiItem3.requestId + " and " + notiItem3.freelancerId + " and " + notiItem3.date)

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

                                        Log.e(TAG, "Swipe to delete user " + dataSnapshot.key.toString())

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

    private fun initSpruce() {

//        ObjectAnimator.ofFloat(
//            recycler_notifi,
//            "translationX",
//            -recycler_notifi.width,
//            0f
//        ).setDuration(800)


//        var objectAnimator = ObjectAnimator()


        spruceAnimator = Spruce.SpruceBuilder(recycler_notifi)
            .sortWith(DefaultSort(100))
            .animateWith(
                DefaultAnimations.shrinkAnimator(recycler_notifi, 800),
//                ObjectAnimator.ofFloat(recycler_notifi, View.TRANSLATION_Y, if (true) 200f else -200f, 0f)

                ObjectAnimator.ofFloat(
                    recycler_notifi,
                    View.TRANSLATION_X,
                    (-recycler_notifi.width).toFloat(),
                    0f
                ).setDuration(800)
            )
            .start()

    }

    private fun loadNotification() {

        progress = Helper(activity).createProgressDialog(getString(R.string.please_wait))
//        progress?.show()


        RefBase.customerNotification().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.ref.removeEventListener(this)
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
//                    for (i in listOfNotification) {
//                        Log.e(TAG, "LISTT3 " + i.orderId)
//                    }
//                    listOfNotification.clear()
//                    listOfOrdersRequest.clear()
//                    listOfCategory.clear()
//                    for (i in listOfNotification) {
//                        Log.e(TAG, "LISTT4 " + i.orderId)
//                    }
                    Log.e(
                        TAG,
                        "Test555 " + dataSnapshot.childrenCount + ", " + dataSnapshot.toString() + "\n"
                    )
                    //.children.withIndex : content of dataSnapshot with index for each row put it into map to loop on them
                    dataSnapshot.children.withIndex()
                        //loop then put only the id (content of dataSnapshot) into notification Model so u can use it to get the children of it
                        .map { indexedValue: IndexedValue<DataSnapshot> ->

                            Log.e(TAG, "test 661 " + indexedValue.value.key.toString())
//                            listOfNotification.add(notification)
//                        Log.e(TAG, listOfNotification.get(indexedValue.index).firebaseUserId)
//                        Log.e(TAG, listOfNotification.get(indexedValue.index).title)
                            //get children of id (indexedValue) "ex: the index of 0 that his value is id="dsdsdsds" get the children of it each one with index (auto generated id)"
                            indexedValue.value.children.withIndex()
                                //loop into children
                                .map { childIndexAt: IndexedValue<DataSnapshot> ->


                                    //                                    notification =
//                                        Notification(
//                                            indexedValue.value.key.toString(),
//                                            "",
//                                            "",
//                                            "",
//                                            false
//                                        )


                                    //Log.e(TAG, "index " + value.value.toString())
                                    //get child "ex: child at index 0 the the value of it is "5555dfssf" get the child that its name is Message "
                                    childIndexAt.value.child(Constants.TITLE)
                                        .let {
                                            notification.title = it.value.toString()
                                            Log.e(TAG, "test777 " + notification.title)
                                        }
                                    childIndexAt.value.child(Constants.MESSAGE)
                                        .let {
                                            notification.message = it.value.toString()
                                            Log.e(TAG, "test666 " + notification.message)
                                        }
                                    childIndexAt.value.child(Constants.ORDERID)
                                        .let {
                                            notification.requestId = it.value.toString()
                                            Log.e(TAG, "test888 " + notification.requestId)
                                        }

                                    childIndexAt.value.child(Constants.SHOWN_NOTI)
                                        .let {
                                            notification.shown = it.value.toString().toBoolean()
                                            Log.e(TAG, "test999 " + notification.shown)

                                        }

                                    listOfNotification.add(notification)
//                                    Log.e(TAG, "NOTIFICATION ccc " + notification.notiType)
//                                    listOfNotification.add(notification)
//                                    listOfNotification.add(notification)
//                                    listOfNotification.add(notification)

//                                    for (i in listOfNotification) {
//                                        Log.e(TAG, "LISTT2 " + i.orderId)
//                                    }
//                                    Log.e(
//                                        TAG,
//                                        "LISTx " + listOfNotification.size + " - " + notification.orderId + listOfNotification[childIndexAt.index].orderId
//                                    )
//                                    if (indexedValue.index == dataSnapshot.children.count() - 1) {
//                                        loadOrderRequestAndCategory()
//                                    }
                                }


//                            if (indexedValue.index == dataSnapshot.children.count() - 1) {
//                                loadOrderRequestAndCategory(null)
//                            }
                        }

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
                        recycler_notifi.layoutManager = linerLayoutManager


//                    adapterNotification.notifyDataSetChanged()
                        progress?.dismiss()
//                    loadOrderRequestAndCategory()
//                    listOfNotification.forEach {
//                        Log.e(TAG, "tytydf" + it.orderId)
//                    }
                    }
                }
//                loadCPNotification(position)
                loadCPNotification(0)
            }

            override fun onCancelled(dataSnapshotError: DatabaseError) {
                Log.e(TAG, "error" + dataSnapshotError.message)
                progress?.dismiss()
            }
        })
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

                            when (keyword) {
                                Constants.REQUESTS -> {
                                    Log.e(TAG, "Requestx$keyword")
                                    title = activity!!.getString(R.string.new_order)
                                    body = activity!!.getString(R.string.new_order_requested)
                                    notification.customerId =
                                        map[Constants.CUSTOMER_ID].toString()
                                    if (map[Constants.ORDER_ID] != null) {
                                        notification.requestId = map[Constants.ORDER_ID].toString()
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

//                            listOfCPNotification.add(notification)
                            listOfNotification.add(notification)

//                            listOfNotification.addAll(listOfCPNotification)

                            Log.e(TAG, "size_8484 =  " + listOfCPNotification.size.toString())


//                    RecyclerNotificationAdapter


//                    for (i in listOfNotification) {
//                        Log.e(TAG, "LISTT3 " + i.orderId)
//                    }
//                    listOfNotification.clear()
//                    listOfOrdersRequest.clear()
//                    listOfCategory.clear()
//                    for (i in listOfNotification) {
//                        Log.e(TAG, "LISTT4 " + i.orderId)
//                    }
                            Log.e(
                                TAG,
                                "Test5550 " + dataSnapshot.childrenCount + ", " + dataSnapshot.toString() + "\n"
                            )

                        }

//                        listOfNotification = listOfNotification.reverse()
//                        Collections.reverse(listOfNotification);
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
                            recycler_notifi.layoutManager = linerLayoutManager

//                    RecyclerNotificationAdapter


//                    for (i in listOfNotification) {
//                        Log.e(TAG, "LISTT3 " + i.orderId)
//                    }
//                    listOfNotification.clear()
//                    listOfOrdersRequest.clear()
//                    listOfCategory.clear()
//                    for (i in listOfNotification) {
//                        Log.e(TAG, "LISTT4 " + i.orderId)
//                    }
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

    private fun loadOrderRequestAndCategory() {

        for (i in listOfNotification) {
            Log.e(TAG, "LISTT " + i.requestId)
        }


        //                    lateinit var orderRequest: OrderRequest
//        Log.e(TAG, "V1" + this.notification?.orderId)
//        Log.e(TAG, "V1" + notification?.orderId)

        listOfNotification.withIndex().map { (index, notification) ->
            Log.e("111111", notification.requestId)
//            RefBase.requests(notification.orderId)
            RefBase.requests(notification.requestId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.removeEventListener(this)
                        if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {

                            Log.e("dsfsf", dataSnapshot.toString())

                            var hashMap: HashMap<String, Object> =
                                dataSnapshot.value as HashMap<String, Object>


                            Log.e(TAG, "STATE1" + hashMap.get(Constants.ORDER_STATE))
                            Log.e(TAG, "ID1" + hashMap.get(Constants.ORDER_ID))

                            var orderRequest: OrderRequest = OrderRequest()

                            orderRequest.state =
                                hashMap.get(Constants.ORDER_STATE).toString()

//                            Log.e(TAG, "V2" + notification.orderId)
//                            Log.e(TAG, "V2" + it.orderId)

                            listOfOrdersRequest.add(orderRequest)
//                            adapterNotification.notifyDataSetChanged()
                            Log.e(
                                TAG,
                                "Requestccc" + orderRequest.state + " - " + listOfOrdersRequest.size
                            )

                            dataSnapshot.child(Constants.CATEGORY_ID).let {
                                var value: DataSnapshot = it
                                Log.e("CATEGORY_ID", it.value.toString())

                                RefBase.category(value.value.toString())
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(dataSnapsho: DataSnapshot) {
                                            Log.e("CATEGORY_IDv", value.value.toString())
                                            dataSnapsho.ref.removeEventListener(this)
//                                            listOfCategory.clear()
                                            if (dataSnapsho.exists() && dataSnapsho.childrenCount > 0) {
//                                                listOfCategory.clear()
                                                Log.e("eee", dataSnapsho.toString())

                                                var hashMap2: HashMap<String, Object> =
                                                    dataSnapsho.value as HashMap<String, Object>

                                                Log.e("CATEGORY_name", hashMap2.toString())

                                                var category: Category = Category()

                                                category.categoryName =
                                                    hashMap2.get(Constants.CATEGORY_NAME)
                                                        .toString()


                                                Log.e(
                                                    "CATEGORY_name2",
                                                    hashMap2.get(Constants.CATEGORY_NAME).toString()
                                                )

                                                listOfCategory.add(category)

//                                                adapterNotification.notifyDataSetChanged()
//                                                val adapter = RecyclerNotificationAdapter(
//                                                    activity,
//                                                    listOfNotification,
//                                                    listOfOrdersRequest,
//                                                    listOfCategory
//                                                )
//                                                Log.e(
//                                                    TAG,
//                                                    " lists" + listOfNotification.size + " - " + listOfOrdersRequest.size + " - " + listOfCategory.size
//                                                )
//                                                recycler_notifi.let {
//                                                    it?.adapter = adapter
//                                                    (it?.adapter as RecyclerNotificationAdapter).notifyDataSetChanged()
//                                                }
//                                                Log.e(TAG, "CAT" + listOfCategory.size)

                                                if (index == listOfNotification.size - 1) {
                                                    adapterNotification!!.notifyDataSetChanged()
                                                    Log.e(TAG, "Final ")
                                                }

                                            }


                                            Log.e(TAG, "CAT2" + listOfCategory.size)

                                        }

                                        override fun onCancelled(p0: DatabaseError) {
                                            Log.e(TAG, p0.message)
                                        }

                                    })

//                            Log.e(TAG, "CAT3" + listOfCategory.size)
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {}
                })
        }


    }

    override fun onRefresh() {
        postDelayed()
    }

    private fun postDelayed() {
        swipeRefreshLayout.isRefreshing = true
        listOfCPNotification.clear()
        listOfNotification.clear()
//        mHandler.postDelayed({
//            swipeRefreshLayout.isRefreshing = false
//            loadNotification()
//            loadCPNotification()
//        }, 1500)

//        loadNotification()
        loadCPNotification(pos)

    }

    override fun onNotificationClciked(notification: Notification, position: Int) {
        Log.e(TAG, "clicked ")

        when (notification.notiType) {
            Constants.REQUESTS -> {
                Log.e(TAG, notification.notiType)

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
            fetchRequestDetails(v, notification)
        }
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.show()

    }

    private fun fetchRequestDetails(
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
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
//

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }


}