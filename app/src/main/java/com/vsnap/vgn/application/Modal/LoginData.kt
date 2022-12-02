package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginData {

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("user_details")
    var UserDetails: LoginUserDetails? = null

}