package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VersionCheckData {

    @SerializedName("update_available")
    @Expose
    var update_available = 0

    @SerializedName("force_update")
    @Expose
    var force_update = 0

    @SerializedName("otp_digit")
    @Expose
    var otp_digit = 0

    @SerializedName("privacy_policy_link")
    @Expose
    var privacy_policy_link: String? = null

    @SerializedName("help_link")
    @Expose
    var help_link: String? = null

    @SerializedName("terms_and_condition")
    @Expose
    var terms_and_condition: String? = null

    @SerializedName("version_alert_title")
    @Expose
    var version_alert_title: String? = null

    @SerializedName("version_alert_content")
    @Expose
    var version_alert_content: String? = null

    @SerializedName("play_store_link")
    @Expose
    var play_store_link: String? = null

    @SerializedName("playstore_market_id")
    @Expose
    var playstore_market_id: String? = null


}