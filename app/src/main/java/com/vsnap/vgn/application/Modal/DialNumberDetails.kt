package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DialNumberDetails {

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("customer_number")
    @Expose
    var customer_number: String? = null

    @SerializedName("alternate_number1")
    @Expose
    var alternate_number1: String? = null


}