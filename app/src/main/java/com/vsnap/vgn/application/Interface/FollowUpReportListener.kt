package com.vsnap.vgn.application.Interface

import com.vsnap.vgn.application.Adapters.FollowUpDetailsReportAdapter
import com.vsnap.vgn.application.Modal.GetFollowUpDetailsData

interface FollowUpReportListener {

    fun onReportClick(holder: FollowUpDetailsReportAdapter.MyViewHolder, data: GetFollowUpDetailsData)

}