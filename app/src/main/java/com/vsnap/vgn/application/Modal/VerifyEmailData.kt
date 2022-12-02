package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerifyEmailData {
    @SerializedName("is_number_exists")
    @Expose
    var is_number_exists: String? = null

    @SerializedName("is_redirect_to_otp")
    @Expose
    var is_redirect_to_otp: String? = null

}