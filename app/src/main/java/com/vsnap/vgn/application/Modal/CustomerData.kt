package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomerData {

    @SerializedName("customer_id")
    @Expose
    var customer_id = 0

    @SerializedName("customer_name")
    @Expose
    var customer_name: String? = null

    @SerializedName("customer_mobile")
    @Expose
    var customer_mobile: String? = null

    @SerializedName("project_name")
    @Expose
    var project_name: String? = null

    @SerializedName("sales_agent_name")
    @Expose
    var sales_agent_name: String? = null

    @SerializedName("crm_agent_name")
    @Expose
    var crm_agent_name: String? = null

    @SerializedName("crm_agent_number")
    @Expose
    var crm_agent_number: String? = null

    @SerializedName("customer_type")
    @Expose
    var customer_type: String? = null

    @SerializedName("created_on")
    @Expose
    var created_on: String? = null

    @SerializedName("file_recording")
    @Expose
    var file_recording: String? = null


}