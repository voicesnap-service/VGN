package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.SerializedName

class AvailablityStatusResponse {

    @SerializedName("status")
    var status = 0

    @SerializedName("message")
    var message :String ? = null

    @SerializedName("data")
    var data : ArrayList<AvailabilityStatusData>? = null
}