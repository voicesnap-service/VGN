package com.vsnap.vgn.application.Interface

import com.vsnap.vgn.application.Adapters.CallHistoryAdapter
import com.vsnap.vgn.application.Modal.CallHistoryData

interface CallHistoryClickListener {
    fun onClick(holder: CallHistoryAdapter.MyViewHolder, data: CallHistoryData)
}