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
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo


class RecyclerNotificationAdapter(
    private val context: Activity?,
    private val listOfNotification: ArrayList<Notification>,
    private val listOfOrderRequest: ArrayList<OrderRequest>,
    private val listOfCategory: ArrayList<Category>,
    private val onNotificationClicked: OnNotificationClicked
) : RecyclerView.Adapter<CustomView>() {


    val TAG = RecyclerNotificationAdapter::class.java.name


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {

        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(com.beyond_tech.seyanah_admin.R.layout.recycler_item2, parent, false)

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



        YoYo.with(Techniques.FadeIn)
            .duration(600)
            .playOn(holder.itemView)

        Log.e(TAG, "check type " + noti.notiType)


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
            body =
                itemView.findViewById<TextView>(com.beyond_tech.seyanah_admin.R.id.tv_notification_body)
            time =
                itemView.findViewById<TextView>(com.beyond_tech.seyanah_admin.R.id.tv_notification_date)
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
