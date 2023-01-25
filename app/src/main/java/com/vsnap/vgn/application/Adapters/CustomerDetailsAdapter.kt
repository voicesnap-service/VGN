package com.vsnap.vgn.application.Adapters
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vsnap.vgn.application.Interface.CustomerCardClickListener
import com.vsnap.vgn.application.Modal.CustomerData
import com.vsnap.vgn.application.R

class CustomerDetailsAdapter(
    var callList: List<CustomerData>,
    var Call_type:String,
    private val context: Context?,
    val Listener: CustomerCardClickListener
) : RecyclerView.Adapter<CustomerDetailsAdapter.MyViewHolder>() {
    private var mExpandedPosition = -1
    companion object {
        var CardClick: CustomerCardClickListener? = null
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
        var lnrMenuIcons1: LinearLayout
        var imgLastCall: ImageView
        var imgUpdate: ImageView
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
            lnrMenuIcons1 = itemView.findViewById(R.id.lnrMenuIcons1)
            imgLastCall = itemView.findViewById(R.id.imgLastCall)
            imgUpdate = itemView.findViewById(R.id.imgUpdate)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_list_item, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = callList[position]
        CardClick = Listener
        CardClick?.onClick(holder, data)
        val isExpanded = position == mExpandedPosition


        if(!data.customer_name!!.equals("")){
            holder.lblMobileNumber!!.setText(data.customer_mobile +" "+ "-"+" "+data.customer_name)

        }else{
            holder.lblMobileNumber!!.setText(data.customer_mobile)
        }
        holder.lblProjectName!!.setText(data.project_name)
        holder.lblAgentName!!.setText(data.sales_agent_name)
        holder.lblRecentCallTime!!.setText(data.customer_type)
        holder.lblRecentCallTime!!.setTextColor(Color.parseColor("#5daf98"))

        holder.rytMenuIcon.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.rytCustomerINFO.isActivated = isExpanded
        holder.rytCustomerINFO.visibility = View.VISIBLE


        holder.rytCallHistory.visibility = View.GONE
        holder.rytUpdateInfo.visibility = View.GONE
        holder.rytBlock.visibility = View.GONE
        holder.rytShare.visibility = View.GONE
        holder.rytrecording.visibility = View.GONE
        holder.imgLastCall.visibility = View.INVISIBLE

        holder.imgUpdate!!.setImageResource(R.drawable.voice_play_icon)

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



