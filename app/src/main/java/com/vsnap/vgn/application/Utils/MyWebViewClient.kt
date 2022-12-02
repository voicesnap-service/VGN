package com.vsnap.vgn.application.Utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.webkit.WebViewClient
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.webkit.WebView

class MyWebViewClient(var context: Activity) : WebViewClient() {
    override fun onReceivedError(
        view: WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        if (errorCode == ERROR_TIMEOUT) {
            view.stopLoading()
        }
        view.loadData(
            "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head>" +
                    "<body><a href=\"" + view.url + "\">Turn on your network connection and click here</a></body></html>",
            "text/html",
            "utf-8"
        )
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (isNetworkAvailable(context)) {
            view.loadUrl(url)
        } else showAlert("Connectivity", "Check Internet Connection")
        return true
    }

    private fun showAlert(title: String, msg: String) {
        val alertDialog = AlertDialog.Builder(
            context
        )
        alertDialog.setCancelable(false)
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)
        alertDialog.setNeutralButton("OK") { dialog, which -> }
        alertDialog.show()
    }

    companion object {
        fun isNetworkAvailable(activity: Activity): Boolean {
            val connectivity = activity
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity == null) {
                return false
            } else {
                val info = connectivity.allNetworkInfo
                if (info != null) {
                    for (anInfo in info) {
                        if (anInfo.state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}