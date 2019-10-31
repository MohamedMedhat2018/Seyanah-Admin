package com.example.serviceadmin.adapters.rv

import android.app.Activity
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.example.serviceadmin.R
import com.example.serviceadmin.adapters.rv.RecyclerNotificationAdapter.CustomView
import com.example.serviceadmin.constants.Constants
import com.example.serviceadmin.fire_utils.RefBase
import com.example.serviceadmin.models.Category
import com.example.serviceadmin.models.Notification
import com.example.serviceadmin.models.OrderRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RecyclerNotificationAdapter(
    private val context: Activity?,
    private val listOfNotification: ArrayList<Notification>,
    private val listOfOrderRequest: ArrayList<OrderRequest>,
    private val listOfCategory: ArrayList<Category>
) : RecyclerView.Adapter<CustomView>() {

    val TAG = RecyclerNotificationAdapter::class.java.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {

        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item2, parent, false)
//        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.notification, parent, false)
//        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item2, parent, )

        return CustomView(view)
    }

    override fun getItemCount(): Int {
        return listOfNotification.size
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {

        val noti = listOfNotification[position]

        Log.e(TAG, "check type " + noti.notiType)
        when (noti.notiType) {

            Constants.USERS, Constants.FREELANCERS  -> {
                holder.title?.text = noti.title
                Log.e(TAG, "title from user or free " + noti.title)

                holder.body?.text = noti.message

                Log.e(TAG, "body from user or free " + noti.message)

            }
            else -> {
                holder.title?.text = noti.title
                Log.e(TAG, "title555" + noti.title)

                holder.body?.text = noti.message

                Log.e(TAG, "body" + noti.message)


//        if (listOfOrderRequest.isNotEmpty() && listOfOrderRequest.size < listOfNotification.size ){
//            val order = listOfOrderRequest[position]
//            holder.state?.text = order.state
//            Log.e(TAG, "vv" + order.state)
//        }
//
//        if (listOfCategory.isNotEmpty() && listOfCategory.size < listOfNotification.size ){
//            val category = listOfCategory[position]
//            holder.category?.text = category.categoryName
//            Log.e(TAG, "vv2" + category.categoryName)
//        }
//

                RefBase.requests(noti.orderId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.removeEventListener(this)
                        if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                            Log.e("dsfsf", dataSnapshot.toString())
//                    var request = dataSnapshot.value as OrderRequest
//                    Log.e(TAG, "Request" + request.categoryId)
                            //var orderRequest = dataSnapshot.value
                            dataSnapshot.child(Constants.CATEGORY_ID).let {
                                Log.e("rutet", it.value.toString())
                                RefBase.category(it.value.toString())
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(dataSnapsho: DataSnapshot) {
                                            dataSnapsho.ref.removeEventListener(this)
                                            if (dataSnapsho.exists() && dataSnapsho.childrenCount > 0) {
                                                dataSnapsho.child(Constants.CATEGORY_NAME).let {
                                                    holder.rlCategory?.visibility = View.VISIBLE
                                                    holder.category?.text = it.value.toString()
                                                }
                                            }
                                        }

                                        override fun onCancelled(p0: DatabaseError) {
                                            Log.e(TAG, p0.message)
                                        }

                                    })

                            }
                            dataSnapshot.child(Constants.ORDER_STATE).let {
                                holder.state?.visibility = View.VISIBLE
                                holder.state?.text = it.value.toString()
                            }

                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }

                })
            }

        }




    }

    class CustomView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView? = null
        var body: TextView? = null
        var category: TextView? = null
        var state: TextView? = null
        var rlCategory: View? = null


        init {
            title = itemView.findViewById<TextView>(R.id.tv_notification_title)
            body = itemView.findViewById<TextView>(R.id.tv_notification_body)
            category = itemView.findViewById<TextView>(R.id.tv_notification_service_name)
            state = itemView.findViewById(R.id.tv_notification_state)
            rlCategory = itemView.findViewById(R.id.rl_for)


        }

    }


}
