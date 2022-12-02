package com.vsnap.vgn.application.Interface

import com.vsnap.vgn.application.Adapters.RecentCallAdapter
import com.vsnap.vgn.application.Modal.RecentCallsData

interface RecentCardClickListener {

    fun onClick(holder: RecentCallAdapter.MyViewHolder, data: RecentCallsData)

}