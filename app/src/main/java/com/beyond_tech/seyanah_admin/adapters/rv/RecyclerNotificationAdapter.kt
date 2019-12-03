package com.beyond_tech.seyanah_admin.adapters.rv

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.beyond_tech.seyanah_admin.adapters.rv.RecyclerNotificationAdapter.CustomView
import com.beyond_tech.seyanah_admin.interfaces.OnNotificationClicked
import com.beyond_tech.seyanah_admin.models.Category
import com.beyond_tech.seyanah_admin.models.Notification
import com.beyond_tech.seyanah_admin.models.OrderRequest


class RecyclerNotificationAdapter(
    private val context: Activity?,
    private val listOfNotification: ArrayList<Notification>,
    private val listOfOrderRequest: ArrayList<OrderRequest>,
    private val listOfCategory: ArrayList<Category>,
    private val onNotificationClicked: OnNotificationClicked
) : RecyclerView.Adapter<CustomView>() {

//    val onNotificationClicked : OnNotificationClicked? = null
    val TAG = RecyclerNotificationAdapter::class.java.name




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {

        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(com.beyond_tech.seyanah_admin.R.layout.recycler_item2, parent, false)
//        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.notification, parent, false)
//        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item2, parent, )

        return CustomView(view)
    }

    override fun getItemCount(): Int {
        return listOfNotification.size
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {

        val noti = listOfNotification[position]


        holder.itemView.setOnClickListener {
            onNotificationClicked.onNotificationClciked(noti, position)
        }


        holder.title?.text = noti.title
        Log.e(TAG, "title from user or free " + noti.title)
        holder.body?.text = noti.message
        holder.time?.text = noti.date
        Log.e(TAG, "body from user or free " + noti.message)


        Log.e(TAG, "check type " + noti.notiType)
//        when (noti.notiType) {
//            Constants.USERS, Constants.FREELANCERS -> {
//                holder.title?.text = noti.title
//                Log.e(TAG, "title from user or free " + noti.title)
//                holder.body?.text = noti.message
//                Log.e(TAG, "body from user or free " + noti.message)
//            }
//            else -> {
//                holder.title?.text = noti.title
//                Log.e(TAG, "title555" + noti.title)
//                holder.body?.text = noti.message
//                Log.e(TAG, "body" + noti.message)
//
//
////        if (listOfOrderRequest.isNotEmpty() && listOfOrderRequest.size < listOfNotification.size ){
////            val order = listOfOrderRequest[position]
////            holder.state?.text = order.state
////            Log.e(TAG, "vv" + order.state)
////        }
////
////        if (listOfCategory.isNotEmpty() && listOfCategory.size < listOfNotification.size ){
////            val category = listOfCategory[position]
////            holder.category?.text = category.categoryName
////            Log.e(TAG, "vv2" + category.categoryName)
////        }
////
//
////                RefBase.requests(noti.orderId).addValueEventListener(object : ValueEventListener {
////                    override fun onDataChange(dataSnapshot: DataSnapshot) {
////                        dataSnapshot.ref.removeEventListener(this)
////                        if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
////                            Log.e("dsfsf", dataSnapshot.toString())
//////                    var request = dataSnapshot.value as OrderRequest
//////                    Log.e(TAG, "Request" + request.categoryId)
////                            //var orderRequest = dataSnapshot.value
////                            dataSnapshot.child(Constants.CATEGORY_ID).let {
////                                Log.e("rutet", it.value.toString())
////                                RefBase.category(it.value.toString())
////                                    .addValueEventListener(object : ValueEventListener {
////                                        override fun onDataChange(dataSnapsho: DataSnapshot) {
////                                            dataSnapsho.ref.removeEventListener(this)
////                                            if (dataSnapsho.exists() && dataSnapsho.childrenCount > 0) {
////                                                dataSnapsho.child(Constants.CATEGORY_NAME).let {
//////                                                    holder.rlCategory?.visibility = View.VISIBLE
////                                                    holder.category?.text = it.value.toString()
////                                                }
////                                            }
////                                        }
////
////                                        override fun onCancelled(p0: DatabaseError) {
////                                            Log.e(TAG, p0.message)
////                                        }
////
////                                    })
////
////                            }
////
////                            dataSnapshot.child(Constants.ORDER_STATE).let {
////                                //                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                                Log.e(TAG, "State " + it.value.toString())
////                                when (it.value.toString()) {
////                                    "POST" -> {
////                                        Helper(context).changeShapeColor(
////                                            context,
////                                            holder.stateIcon!!.background,
////                                            blue
////                                        )
////                                        Log.e(TAG, "BG CARD")
////                                        holder.cardBg?.setCardBackgroundColor(
////                                            ContextCompat.getColor(
////                                                context!!,
////                                                blue
////                                            )
////                                        )
////                                    }iv_notification_state_icon
////                                    "CM_FINISHED" -> {
////                                        Helper(context).changeShapeColor(
////                                            context,
////                                            holder.stateIcon!!.background,
////                                            dodgerblue
////                                        )
////                                        holder.cardBg?.setCardBackgroundColor(
////                                            ContextCompat.getColor(
////                                                context!!,
////                                                dodgerblue
////                                            )
////                                        )
////
////                                    }
////                                    "FREELANCER_WORKING" -> {
////                                        Helper(context).changeShapeColor(
////                                            context,
////                                            holder.stateIcon!!.background,
////                                            orange_light3
////                                        )
////                                        holder.cardBg?.setCardBackgroundColor(
////                                            ContextCompat.getColor(
////                                                context!!,
////                                                orange_light3
////                                            )
////                                        )
////
////                                    }
////                                    "FREELANCE_FINISHED" -> {
////                                        Helper(context).changeShapeColor(
////                                            context,
////                                            holder.stateIcon!!.background,
////                                            lightgreen
////                                        )
////                                        holder.cardBg?.setCardBackgroundColor(
////                                            ContextCompat.getColor(
////                                                context!!,
////                                                lightgreen
////                                            )
////                                        )
////
////                                    }
//////                                        "CM_FINISHED" ->  DrawableCompat.setTint(holder.stateIcon!!.drawable, ContextCompat.getColor(context!!, R.color.dodgerblue))
//////                                        "FREELANCER_WORKING" ->  DrawableCompat.setTint(holder.stateIcon!!.drawable, ContextCompat.getColor(context!!, R.color.orange_light3))
//////                                        "FREELANCE_FINISHED" ->  DrawableCompat.setTint(holder.stateIcon!!.drawable, ContextCompat.getColor(context!!, R.color.lightgreen))
////                                    else -> {
////                                        Log.e(TAG, "No value " + it.value.toString())
////
////                                    }
////                                }
//////                                }
////
////                                holder.state?.visibility = View.VISIBLE
////                                holder.stateIcon?.visibility = View.VISIBLE
////                                holder.state?.text = it.value.toString()
////                            }
////
////
////                        }
////                    }
////
////                    override fun onCancelled(p0: DatabaseError) {
////
////                    }
////
////                })
//            }
//
//        }


    }


    class CustomView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView? = null
        var body: TextView? = null
        var time: TextView? = null
        var category: TextView? = null
        var state: TextView? = null
        var rlCategory: View? = null
        var stateIcon: View? = null
        var cardBg: CardView? = null


        init {
            title =
                itemView.findViewById<TextView>(com.beyond_tech.seyanah_admin.R.id.tv_notification_title)
            body = itemView.findViewById<TextView>(com.beyond_tech.seyanah_admin.R.id.tv_notification_body)
            time = itemView.findViewById<TextView>(com.beyond_tech.seyanah_admin.R.id.tv_notification_date)
            category =
                itemView.findViewById<TextView>(com.beyond_tech.seyanah_admin.R.id.tv_notification_service_name)
            state = itemView.findViewById(com.beyond_tech.seyanah_admin.R.id.tv_notification_state)
            rlCategory = itemView.findViewById(com.beyond_tech.seyanah_admin.R.id.rl_for)
            stateIcon =
                itemView.findViewById<View>(com.beyond_tech.seyanah_admin.R.id.iv_notification_state_icon)

            cardBg = itemView.findViewById<CardView>(com.beyond_tech.seyanah_admin.R.id.cv_item)
            cardBg = itemView.findViewById<CardView>(com.beyond_tech.seyanah_admin.R.id.cv_item)


        }

    }


}
