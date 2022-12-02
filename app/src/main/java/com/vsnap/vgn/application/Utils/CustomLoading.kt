package com.vsnap.vgn.application.Utils

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.view.WindowManager
import android.graphics.drawable.ColorDrawable
import com.vsnap.vgn.application.R

object CustomLoading {
    fun createProgressDialog(context: Context?): ProgressDialog {
        val dialog = ProgressDialog(context)
        try {
            dialog.show()
        } catch (e: WindowManager.BadTokenException) {
        }
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_loading)
        // dialog.setMessage(Message);
        return dialog
    }
}