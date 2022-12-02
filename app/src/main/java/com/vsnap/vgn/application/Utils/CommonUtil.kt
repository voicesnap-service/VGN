package com.vsnap.vgn.application.Utils

import android.R
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.vsnap.vgn.application.Activities.EnterEmail
import com.vsnap.vgn.application.Modal.LoginUserDetails
import com.vsnap.vgn.application.Modal.VerifyEmailData


object CommonUtil {

    var popuptermsNcondition: PopupWindow? = null
    var WebURl: String? = null
    var TermsAndCondition: String? = null
    var HelpUrl: String? = null
    var DeviceType: String? = "android"
    var LogoutType: Int? = 0
    var UserData: LoginUserDetails? = null
    var VerifyEmailData: List<VerifyEmailData> = ArrayList()

    var CallAvailableType:String?=null
    fun isNetworkConnected(activity: Activity): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    fun CommonAlertOk(activity: Activity?, msg: String?) {
        if (activity != null) {
            val dlg = AlertDialog.Builder(activity)
            dlg.setTitle("Info")
            dlg.setMessage(msg)
            dlg.setPositiveButton("OK") { dialog, which -> }
            dlg.setCancelable(false)
            dlg.create()
            dlg.show()
        }
    }

    fun AlertFinishOk(activity: Activity?, msg: String?) {
        if (activity != null) {
            val dlg = AlertDialog.Builder(activity)
            dlg.setTitle("Info")
            dlg.setMessage(msg)
            dlg.setPositiveButton("OK") { dialog, which ->
                activity.onBackPressed()
            }
            dlg.setCancelable(false)
            dlg.create()
            dlg.show()
        }
    }


    fun passwordAlert(activity: Activity?) {
        if (activity != null) {
            val image = ImageView(activity)
            image.setImageResource(R.drawable.ic_lock_lock)

            val alert1 = "- Include lowerCase Or upperCase characters"
            val alert2 = "- Include Special characters"
            val alert3 = "- To be Atleast 8 characters long"
            val dlg = AlertDialog.Builder(activity)
            dlg.setTitle("Your password need to ")
            dlg.setMessage(alert1 + "\n" + alert2 + "\n" + alert3)
            dlg.setPositiveButton("OK") { dialog, which -> }
            dlg.setCancelable(false)
            dlg.create()
            dlg.show()
        }
    }

    fun ApiAlert(activity: Activity?, msg: String?) {
        if (activity != null) {
            val dlg = AlertDialog.Builder(activity)
            dlg.setTitle("Info")
            dlg.setMessage(msg)
            dlg.setPositiveButton("OK") { dialog, which -> }
            dlg.setCancelable(false)
            dlg.create()
            dlg.show()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun TermsAndConditions(activity: Activity?, value: Boolean) {
        val inflater =
            activity!!.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout =
            inflater.inflate(com.vsnap.vgn.application.R.layout.activity_terms_condition, null)
        popuptermsNcondition = PopupWindow(
            layout,
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT,
            true
        )
        popuptermsNcondition!!.contentView = layout
        popuptermsNcondition!!.showAtLocation(layout, Gravity.CENTER, 0, 0)
        val webview = layout.findViewById<WebView>(com.vsnap.vgn.application.R.id.webview)
        val btnTerms =
            layout.findViewById<Button>(com.vsnap.vgn.application.R.id.btnTermsAndCondition)
        val termsCheckbox =
            layout.findViewById<CheckBox>(com.vsnap.vgn.application.R.id.termsCheckbox)
        val LayoutHeader =
            layout.findViewById<ConstraintLayout>(com.vsnap.vgn.application.R.id.LayoutHeader)
        val imgBack = layout.findViewById<ImageView>(com.vsnap.vgn.application.R.id.imgBack)

        LayoutHeader.visibility = View.GONE

//        WebURl = "https://gradit.voicesnap.com/Home/TermsAndConditions"


        imgBack.setOnClickListener({

            popuptermsNcondition!!.dismiss()
        })

        val progressDialog = CustomLoading.createProgressDialog(activity)
        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                progressDialog.show()
//                setProgress(progress * 100)
                if (progress == 100) {
                    progressDialog.dismiss()
                }
            }
        }
        webview.webViewClient = MyWebViewClient(activity)
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings = webview.settings
        webSettings.loadsImagesAutomatically = true
        webSettings.builtInZoomControls = true
        webSettings.javaScriptEnabled = true
        webview.loadUrl(HelpUrl!!)
        progressDialog.dismiss()


    }

    @SuppressLint("SetJavaScriptEnabled")
    fun WebUrload(activity: Activity?, url: String) {
        val inflater =
            activity!!.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout =
            inflater.inflate(com.vsnap.vgn.application.R.layout.activity_terms_condition, null)
        popuptermsNcondition = PopupWindow(
            layout,
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT,
            true
        )
        popuptermsNcondition!!.contentView = layout
        popuptermsNcondition!!.showAtLocation(layout, Gravity.CENTER, 0, 0)
        val webview = layout.findViewById<WebView>(com.vsnap.vgn.application.R.id.webview)
        val btnTerms =
            layout.findViewById<Button>(com.vsnap.vgn.application.R.id.btnTermsAndCondition)
        val termsCheckbox =
            layout.findViewById<CheckBox>(com.vsnap.vgn.application.R.id.termsCheckbox)
        val LayoutHeader =
            layout.findViewById<ConstraintLayout>(com.vsnap.vgn.application.R.id.LayoutHeader)
        val imgBack = layout.findViewById<ImageView>(com.vsnap.vgn.application.R.id.imgBack)

        LayoutHeader.visibility = View.VISIBLE

        imgBack.setOnClickListener({

            popuptermsNcondition!!.dismiss()
        })

        val progressDialog = CustomLoading.createProgressDialog(activity)
        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                progressDialog.show()
//                setProgress(progress * 100)
                if (progress == 100) {
                    progressDialog.dismiss()
                }
            }
        }
        webview.webViewClient = MyWebViewClient(activity)
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings = webview.settings
        webSettings.loadsImagesAutomatically = true
        webSettings.builtInZoomControls = true
        webSettings.javaScriptEnabled = true
        webview.loadUrl(url)
        progressDialog.dismiss()


    }
}