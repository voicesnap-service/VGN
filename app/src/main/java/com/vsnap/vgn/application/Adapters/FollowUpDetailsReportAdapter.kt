package com.vsnap.vgn.application.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vsnap.vgn.application.Interface.FollowUpReportListener
import com.vsnap.vgn.application.Modal.GetFollowUpDetailsData
import com.vsnap.vgn.application.R


class FollowUpDetailsReportAdapter(
    var callList: List<GetFollowUpDetailsData>,
    private val context: Context?,
    val Listener: FollowUpReportListener
) : RecyclerView.Adapter<FollowUpDetailsReportAdapter.MyViewHolder>() {

    companion object {
        var CardClick: FollowUpReportListener? = null
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var lblMobileNumber: TextView
        var lblProjectName: TextView
        var lblRecentCallTime: TextView
        var lblAgentName: TextView
        var rytCustomerINFO: RelativeLayout
        var btnFollowUpCompleted: Button
        init {
            lblMobileNumber = itemView.findViewById(R.id.lblMobileNumber)
            lblProjectName = itemView.findViewById(R.id.lblProjectName)
            lblAgentName = itemView.findViewById(R.id.lblAgentName)
            lblRecentCallTime = itemView.findViewById(R.id.lblRecentCallTime)
            rytCustomerINFO = itemView.findViewById(R.id.rytCustomerINFO)
            btnFollowUpCompleted = itemView.findViewById(R.id.btnFollowUpCompleted)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_followup_report_ui, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = callList[position]
        CardClick = Listener
        CardClick?.onReportClick(holder, data)
        if(!data.customer_name.equals("")){
            holder.lblMobileNumber!!.setText(data.cusomer_mobile +" "+ "-"+" "+data.customer_name)
        }else{
            holder.lblMobileNumber!!.setText(data.cusomer_mobile)
        }
        holder.lblProjectName!!.setText(data.project_name)
        holder.lblAgentName!!.setText(data.agent_name)
        holder.lblRecentCallTime!!.setText(data.follow_up_time+"-"+data.follow_up_time)
        if(data.is_followup_marked.equals("0")){
            holder.btnFollowUpCompleted.visibility=View.VISIBLE
        }else{
            holder.btnFollowUpCompleted.visibility=View.GONE

        }


    }

    override fun getItemCount(): Int {
        return callList.size

    }

}


