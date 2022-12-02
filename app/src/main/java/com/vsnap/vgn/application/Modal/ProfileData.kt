package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileData {
    @SerializedName("agent_id")
    @Expose
    var agent_id = 0

    @SerializedName("member_name")
    @Expose
    var member_name: String? = null

    @SerializedName("mobile_number")
    @Expose
    var mobile_number: String? = null

    @SerializedName("email_id")
    @Expose
    var email_id: String? = null

    @SerializedName("user_type")
    @Expose
    var user_type: String? = null

    @SerializedName("call_availability")
    @Expose
    var call_availability: String? = null

    @SerializedName("reporting_to")
    @Expose
    var reporting_to: String? = null

    @SerializedName("soft_phone_ip")
    @Expose
    var soft_phone_ip: String? = null
}