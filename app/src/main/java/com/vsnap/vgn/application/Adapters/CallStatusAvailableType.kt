package com.vsnap.vgn.application.Adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.vsnap.vgn.application.Interface.CallStatusListener
import com.vsnap.vgn.application.Modal.AvailabilityStatusData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Utils.CommonUtil

class CallStatusAvailableType(
    var CallStatusList: List<AvailabilityStatusData>,
    private val context: Context?,
    val Listener: CallStatusListener
) : RecyclerView.Adapter<CallStatusAvailableType.MyViewHolder>() {

    companion object {
        var StatusClick: CallStatusListener? = null
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var lblAvailabilityType: TextView
        var layoutAvailablestatus: ConstraintLayout
        init {
            lblAvailabilityType = itemView.findViewById(R.id.lblstatusAvailabilityType)
            layoutAvailablestatus = itemView.findViewById(R.id.layoutAvailablestatus)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.availability_status_ui, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = CallStatusList[position]
        StatusClick = Listener
        StatusClick?.onStatusClick(holder, data)
        holder.lblAvailabilityType!!.setText(data.unnest)

        if (data.unnest.equals("Available")) {
            holder.layoutAvailablestatus.setBackgroundResource(R.drawable.bg_available_outline_green)
            holder.lblAvailabilityType.setTextColor(Color.parseColor("#3a914b"))
        } else {
            holder.layoutAvailablestatus.setBackgroundResource(R.drawable.bg_available_outline_red)
            holder.lblAvailabilityType.setTextColor(Color.parseColor("#c92424"))
        }


        if (data.unnest!!.equals(CommonUtil.CallAvailableType!!)) {

            if(CommonUtil.CallAvailableType.equals("Available")){
                holder.layoutAvailablestatus.setBackgroundResource(R.drawable.bg_available_selected_green)
                holder.lblAvailabilityType.setTextColor(
                    Color.parseColor(
                        "#FFFFFF"
                    )
                )
            }else{
                holder.layoutAvailablestatus.setBackgroundResource(R.drawable.bg_available_selected_red)
                holder.lblAvailabilityType.setTextColor(
                    Color.parseColor(
                        "#FFFFFF"
                    )
                )
            }


        }
    }

    override fun getItemCount(): Int {
        return CallStatusList.size

    }

}



