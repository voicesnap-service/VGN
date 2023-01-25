package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecentCallsData {
    @SerializedName("customer_id")
    @Expose
    var customer_id = 0

    @SerializedName("customer_name")
    @Expose
    var customer_name: String? = null

    @SerializedName("project_id")
    @Expose
    var project_id = 0

    @SerializedName("project_name")
    @Expose
    var project_name: String? = null

    @SerializedName("conference_id")
    @Expose
    var conference_id = 0

    @SerializedName("cusomer_mobile")
    @Expose
    var cusomer_mobile: String? = null

    @SerializedName("email_id")
    @Expose
    var email_id: String? = null

    @SerializedName("recent_call_time")
    @Expose
    var recent_call_time: String? = null

    @SerializedName("follow_up_date")
    @Expose
    var follow_up_date: String? = null

    @SerializedName("follow_up_time")
    @Expose
    var follow_up_time: String? = null

    @SerializedName("action_taken")
    @Expose
    var action_taken: String? = null

    @SerializedName("file_recording")
    @Expose
    var file_recording: String? = null

    @SerializedName("agent_id")
    @Expose
    var agent_id: String? = null

    @SerializedName("agent_name")
    @Expose
    var agent_name: String? = null

    @SerializedName("is_click_to_call_made")
    @Expose
    var is_click_to_call_made: String? = null

    @SerializedName("recent_call_status")
    @Expose
    var recent_call_status: String? = null


}