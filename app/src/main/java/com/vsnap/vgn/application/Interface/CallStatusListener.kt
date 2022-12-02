package com.vsnap.vgn.application.Interface

import com.vsnap.vgn.application.Adapters.CallStatusAvailableType
import com.vsnap.vgn.application.Modal.AvailabilityStatusData

interface CallStatusListener {

    fun onStatusClick(holder: CallStatusAvailableType.MyViewHolder, data: AvailabilityStatusData)

}