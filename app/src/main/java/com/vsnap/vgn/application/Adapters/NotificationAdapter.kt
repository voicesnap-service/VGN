package com.vsnap.vgn.application.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.vsnap.vgn.application.Modal.NotificationData
import com.vsnap.vgn.application.R
import java.util.ArrayList

class NotificationAdapter constructor(data: List<NotificationData>, context: Context) :
    RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    var notificationList: List<NotificationData> = ArrayList()
    var context: Context
    var Position: Int = 0
    public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.notification_list_ui, parent, false)
        return MyViewHolder(itemView)
    }

    public override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: NotificationData = notificationList.get(position)
        Position = holder.getAbsoluteAdapterPosition()
        holder.lblNotificationType!!.setText(data.notification_type)
        holder.lblNotificationContent!!.setText(data.notification_message)
        holder.lblNoticeficationTime!!.setText(data.notification_time)
        holder.lblDate!!.setText(data.notification_date)

    }

    public override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class MyViewHolder constructor(itemView: View?) : RecyclerView.ViewHolder(
        (itemView)!!
    ) {
        @JvmField
        @BindView(R.id.lblNotificationType)
        var lblNotificationType: TextView? = null

        @JvmField
        @BindView(R.id.lblNotificationContent)
        var lblNotificationContent: TextView? = null

        @JvmField
        @BindView(R.id.lblNoticeficationTime)
        var lblNoticeficationTime: TextView? = null

        @JvmField
        @BindView(R.id.lblDate)
        var lblDate: TextView? = null

        init {
            ButterKnife.bind(this, (itemView)!!)
        }
    }

    init {
        notificationList = data
        this.context = context
    }
}