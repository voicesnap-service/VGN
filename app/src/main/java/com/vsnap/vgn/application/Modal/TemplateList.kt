package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TemplateList {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("template_id")
    @Expose
    var template_id: String? = null

    @SerializedName("template_text")
    @Expose
    var template_text: String? = null

}