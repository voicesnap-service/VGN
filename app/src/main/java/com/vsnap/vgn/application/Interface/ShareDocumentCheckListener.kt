package com.vsnap.vgn.application.Interface

import com.vsnap.vgn.application.Modal.GetShareDocumentData

interface ShareDocumentCheckListener {
    fun add(data: GetShareDocumentData?)
    fun remove(data: GetShareDocumentData?)
}
