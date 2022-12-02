package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginUserDetails {

    @SerializedName("member_id")
    @Expose
    var member_id = 0

    @SerializedName("member_name")
    @Expose
    var member_name: String? = null

    @SerializedName("mobile_number")
    @Expose
    var mobile_number: String? = null

    @SerializedName("member_email")
    @Expose
    var member_email: String? = null

    @SerializedName("organisation_id")
    @Expose
    var organisation_id: String? = null

    @SerializedName("organisation_name")
    @Expose
    var organisation_name: String? = null

    @SerializedName("organisation_logo")
    @Expose
    var organisation_logo: String? = null

    @SerializedName("soft_phone_ip")
    @Expose
    var soft_phone_ip: String? = null


    @SerializedName("user_type")
    @Expose
    var user_type = 0

    @SerializedName("user_type_description")
    @Expose
    var user_type_description: String? = null

    @SerializedName("phone_type")
    @Expose
    var phone_type: String? = null

    @SerializedName("call_availability")
    @Expose
    var call_availability = 0




}