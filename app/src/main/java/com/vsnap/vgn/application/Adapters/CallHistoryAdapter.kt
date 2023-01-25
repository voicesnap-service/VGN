package com.vsnap.vgn.application.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import com.vsnap.vgn.application.Interface.CallHistoryClickListener
import com.vsnap.vgn.application.Modal.CallHistoryData
import com.vsnap.vgn.application.R
import java.util.ArrayList

class CallHistoryAdapter constructor(data: List<CallHistoryData>, context: Context, listener: CallHistoryClickListener) :
    RecyclerView.Adapter<CallHistoryAdapter.MyViewHolder>() {
    var notificationList: List<CallHistoryData> = ArrayList()
    var context: Context
    var Position: Int = 0
    var Listener : CallHistoryClickListener = listener

    companion object {
        var CardClick: CallHistoryClickListener? = null
    }

    public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.call_history, parent, false)
        return MyViewHolder(itemView)
    }

    public override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data: CallHistoryData = notificationList.get(position)
        CardClick = Listener
        CardClick?.onClick(holder, data)

        Position = holder.getAbsoluteAdapterPosition()
        holder.lblProjectName!!.setText(data.project_name)
        holder.lblAgentName!!.setText(data.agent_name)
        holder.lblRecentCallTime!!.setText(data.called_date+" - "+data.called_time)

        if(!data.call_status.equals("")){
            holder.rytRecentCallStatus!!.visibility = View.VISIBLE
            holder.lblRecentCallStatus!!.setText(data.call_status)
        }
        else{
            holder.rytRecentCallStatus!!.visibility = View.GONE
        }

        if(data.file_recording.isNullOrEmpty()){
            holder.rytPlayVoice!!.visibility = View.GONE
        }
        else{
            holder.rytPlayVoice!!.visibility = View.VISIBLE
        }

        Glide.with(context)
            .load(data.project_logo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imgproject_logo!!)

    }

    public override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class MyViewHolder constructor(itemView: View?) : RecyclerView.ViewHolder(
        (itemView)!!
    ) {
        @JvmField
        @BindView(R.id.lblAgentName)
        var lblAgentName: TextView? = null

        @JvmField
        @BindView(R.id.lblRecentCallTime)
        var lblRecentCallTime: TextView? = null

        @JvmField
        @BindView(R.id.lblProjectName)
        var lblProjectName: TextView? = null

        @JvmField
        @BindView(R.id.project_logo)
        var imgproject_logo: ShapeableImageView? = null

        @JvmField
        @BindView(R.id.lblRecentCallStatus)
        var lblRecentCallStatus: TextView? = null

        @JvmField
        @BindView(R.id.rytRecentCallStatus)
        var rytRecentCallStatus: RelativeLayout? = null

        @JvmField
        @BindView(R.id.rytPlayVoice)
        var rytPlayVoice: RelativeLayout? = null

        init {
            ButterKnife.bind(this, (itemView)!!)
        }
    }

    init {
        notificationList = data
        this.context = context
    }
}