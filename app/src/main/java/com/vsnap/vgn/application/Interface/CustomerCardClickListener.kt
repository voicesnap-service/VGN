package com.vsnap.vgn.application.Interface

import com.vsnap.vgn.application.Adapters.CustomerDetailsAdapter
import com.vsnap.vgn.application.Adapters.RecentCallAdapter
import com.vsnap.vgn.application.Modal.CustomerData

interface CustomerCardClickListener {
    fun onClick(holder: CustomerDetailsAdapter.MyViewHolder, data: CustomerData)
}