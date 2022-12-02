package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetCustomerInfoData {

    @SerializedName("customer_id")
    @Expose
    var customer_id = 0

    @SerializedName("customer_name")
    @Expose
    var customer_name: String? = null

    @SerializedName("customer_email")
    @Expose
    var customer_email: String? = null

    @SerializedName("customer_mobile")
    @Expose
    var customer_mobile: String? = null

    @SerializedName("customer_pincode")
    @Expose
    var customer_pincode: String? = null

    @SerializedName("customer_alternate_mobile")
    @Expose
    var customer_alternate_mobile: String? = null

    @SerializedName("customer_area")
    @Expose
    var customer_area: String? = null

    @SerializedName("follow_up_date")
    @Expose
    var follow_up_date: String? = null

    @SerializedName("follow_up_time")
    @Expose
    var follow_up_time: String? = null

    @SerializedName("action_taken")
    @Expose
    var action_taken: String? = null
}