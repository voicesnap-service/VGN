package com.vsnap.vgn.application.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetShareDocumentData {

    @SerializedName("document_id")
    @Expose
    var document_id = 0

    @SerializedName("document_name")
    @Expose
    var document_name: String? = null

    @SerializedName("document_description")
    @Expose
    var document_description: String? = null

    @SerializedName("file_name")
    @Expose
    var file_name: String? = null

    @SerializedName("file_url")
    @Expose
    var file_url: String? = null

    @SerializedName("file_type")
    @Expose
    var file_type: String? = null


    var selectStatus:Boolean? = null

}