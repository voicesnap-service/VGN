package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CallHistoryData {

    @SerializedName("project_id")
    @Expose
    var project_id = 0

    @SerializedName("called_time")
    @Expose
    var called_time: String? = null

    @SerializedName("called_date")
    @Expose
    var called_date: String? = null

    @SerializedName("project_name")
    @Expose
    var project_name: String? = null

    @SerializedName("agent_name")
    @Expose
    var agent_name: String? = null

    @SerializedName("project_logo")
    @Expose
    var project_logo: String? = null

    @SerializedName("agent_id")
    @Expose
    var agent_id = 0

    @SerializedName("call_status")
    @Expose
    var call_status: String? = null

    @SerializedName("file_recording")
    @Expose
    var file_recording: String? = null


}