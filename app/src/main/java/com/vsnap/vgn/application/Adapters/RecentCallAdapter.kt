package com.vsnap.vgn.application.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vsnap.vgn.application.Interface.RecentCardClickListener
import com.vsnap.vgn.application.Modal.RecentCallsData
import com.vsnap.vgn.application.R


class RecentCallAdapter(
    var callList: List<RecentCallsData>,
    var Call_type:String,
    private val context: Context?,
    val Listener: RecentCardClickListener
) : RecyclerView.Adapter<RecentCallAdapter.MyViewHolder>() {
    private var mExpandedPosition = -1
    companion object {
        var CardClick: RecentCardClickListener? = null
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var lblMobileNumber: TextView
        var lblProjectName: TextView
        var lblRecentCallTime: TextView
        var lblAgentName: TextView
        var rytCustomerINFO: RelativeLayout
        var rytMenuIcon: RelativeLayout
        var rytCallHistory: RelativeLayout
        var rytUpdateInfo: RelativeLayout
        var rytCallCustomer: RelativeLayout
        var rytBlock: RelativeLayout
        var rytShare: RelativeLayout
        var imgArrowdown: ImageView
        var rytrecording: RelativeLayout
        var lnrCustomerInfo: LinearLayout
        var lblRecentCallStatus: TextView
        var rytRecentCallStatus: RelativeLayout
        init {
            lblMobileNumber = itemView.findViewById(R.id.lblMobileNumber)
            lblProjectName = itemView.findViewById(R.id.lblProjectName)
            lblAgentName = itemView.findViewById(R.id.lblAgentName)
            lblRecentCallTime = itemView.findViewById(R.id.lblRecentCallTime)
            rytCustomerINFO = itemView.findViewById(R.id.rytCustomerINFO)
            rytMenuIcon = itemView.findViewById(R.id.rytMenuIcon)
            imgArrowdown = itemView.findViewById(R.id.imgArrowdown)
            rytCallHistory = itemView.findViewById(R.id.rytCallHistory)
            rytUpdateInfo = itemView.findViewById(R.id.rytUpdateInfo)
            rytCallCustomer = itemView.findViewById(R.id.rytCallCustomer)
            rytBlock = itemView.findViewById(R.id.rytBlock)
            rytShare = itemView.findViewById(R.id.rytShare)
            rytrecording = itemView.findViewById(R.id.rytrecording)
            lnrCustomerInfo = itemView.findViewById(R.id.lnrCustomerInfo)
            lblRecentCallStatus = itemView.findViewById(R.id.lblRecentCallStatus)
            rytRecentCallStatus = itemView.findViewById(R.id.rytRecentCallStatus)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_customer_info_ui, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = callList[position]
        CardClick = Listener
        CardClick?.onClick(holder, data)
        val isExpanded = position == mExpandedPosition


        if(Call_type.equals("Missed")) {
            if (data.is_click_to_call_made!!.equals("1")) {
                holder.lnrCustomerInfo.setBackgroundResource(R.drawable.bg_click_to_call_made);
            }
        }

        if(!data.customer_name!!.equals("")){
            holder.lblMobileNumber!!.setText(data.cusomer_mobile +" "+ "-"+" "+data.customer_name)

        }else{
            holder.lblMobileNumber!!.setText(data.cusomer_mobile)

        }

        holder.lblProjectName!!.setText(data.project_name)
        holder.lblRecentCallTime!!.setText(data.recent_call_time)
        holder.lblAgentName!!.setText(data.agent_name)

        if(!data.recent_call_status.equals("")){
            holder.rytRecentCallStatus!!.visibility = View.VISIBLE
            holder.lblRecentCallStatus!!.setText(data.recent_call_status)
        }
        else{
            holder.rytRecentCallStatus!!.visibility = View.GONE
        }

        holder.rytMenuIcon.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.rytCustomerINFO.isActivated = isExpanded
        holder.rytCustomerINFO.visibility = View.VISIBLE

        if (isExpanded) {
            holder.imgArrowdown!!.setImageResource(R.drawable.ic_arrow_up_blue)
        } else {
            holder.imgArrowdown!!.setImageResource(R.drawable.ic_arrow_down_blue)
        }
        holder.rytCustomerINFO!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                mExpandedPosition = if (isExpanded) -1 else position
                notifyDataSetChanged()
            }
        })
    }

    override fun getItemCount(): Int {
        return callList.size

    }

}



