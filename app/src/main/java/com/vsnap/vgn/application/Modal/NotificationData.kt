package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationData {

    @SerializedName("notification_id")
    @Expose
    var notification_id = 0

    @SerializedName("notification_message")
    @Expose
    var notification_message: String? = null

    @SerializedName("notification_type")
    @Expose
    var notification_type: String? = null

    @SerializedName("notification_date")
    @Expose
    var notification_date: String? = null

    @SerializedName("notification_time")
    @Expose
    var notification_time: String? = null

}