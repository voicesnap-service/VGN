package com.vsnap.vgn.application.Activities

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.vsnap.vgn.application.Adapters.CallHistoryAdapter
import com.vsnap.vgn.application.Adapters.CustomerDetailsAdapter
import com.vsnap.vgn.application.Interface.CallHistoryClickListener
import com.vsnap.vgn.application.Interface.CustomerCardClickListener
import com.vsnap.vgn.application.Modal.CallHistoryData
import com.vsnap.vgn.application.Modal.CustomerData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.CustomLoading
import com.vsnap.vgn.application.Utils.MyWebViewClient
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Dashboard
import java.util.ArrayList

class CallHistory : AppCompatActivity() {

    var notificationadapter: CallHistoryAdapter? = null

    @JvmField
    @BindView(R.id.rvNotification)
    var rvNotification: RecyclerView? = null

    @JvmField
    @BindView(R.id.NestedScroll)
    var NestedScroll: NestedScrollView? = null

    var dashboardViewmodel: Dashboard? = null
    var Offset: Int = 0
    var TotalCountData: Int = 0
    var PageLimit: Int = 4
    var SearchKeyWord: String? = null
    var GetCallHistoryLiveData: List<CallHistoryData> = ArrayList()

    var VoiceFilePath: String? = null
    var imgclose: ImageView? = null
    var popupWindow: PopupWindow? = null
    var webview: WebView? = null
    var btnDownload: Button? = null
    var mediaPlayer: MediaPlayer? = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        ButterKnife.bind(this)

        dashboardViewmodel = ViewModelProvider(this).get(Dashboard::class.java)
        dashboardViewmodel!!.init()

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.actionbar_layout)
        val view = supportActionBar!!.customView
        val lblTitle = view.findViewById<View>(R.id.lblTitle) as TextView
        val imgBack = view.findViewById<View>(R.id.imgBack) as ImageView
        val rytAction = view.findViewById<View>(R.id.rytAction) as RelativeLayout

        lblTitle.text = "Call History"

        imgBack.setOnClickListener({
            onBackPressed()
        })

        GetTotalCountCallHistory()

        dashboardViewmodel!!.GetCallHistoryCountLiveData!!.observe(this) { response ->
            if (response != null) {
                Log.d("notification","test")
                val status = response.status
                val message = response.message
                TotalCountData = response.data!!.size

                if (status == 1) {
                    Log.d("TotalCountData",TotalCountData.toString())
                    CallHistoryRequest()

                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            }
        }

        dashboardViewmodel!!.CallHistoryLiveData!!.observe(this) { response ->
            if (response != null) {
                Log.d("notification","test")
                val status = response.status
                val message = response.message
                if (status == 1) {
                    GetCallHistoryLiveData = response.data!!
                    Log.d("history_size",GetCallHistoryLiveData.size.toString())

                    notificationadapter = CallHistoryAdapter(GetCallHistoryLiveData, this,
                        object : CallHistoryClickListener {
                            override fun onClick(
                                holder: CallHistoryAdapter.MyViewHolder,
                                data: CallHistoryData
                            ) {

                                holder.rytPlayVoice!!.setOnClickListener({
                                    var id = data.project_id
                                    VoiceFilePath = data.file_recording
                                    if (VoiceFilePath.isNullOrEmpty()) {
                                        Toast.makeText(applicationContext,"No voice file",Toast.LENGTH_SHORT).show()
                                    } else {
                                        PopUpVoicePlay(it, id.toString())
                                    }

                                })

                            }
                        })


                    val mLayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this)
                    rvNotification!!.layoutManager = mLayoutManager
                    rvNotification!!.itemAnimator = DefaultItemAnimator()
                    rvNotification!!.adapter = notificationadapter
                    rvNotification!!.recycledViewPool.setMaxRecycledViews(0, 80)
                    notificationadapter!!.notifyDataSetChanged()
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            }
        }
        NestedScroll!!.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    var childvalue = v.getChildAt(0).measuredHeight - v.measuredHeight
                    Offset += 10
                    Log.d("OffsetSize", Offset.toString())
                    if(Offset < TotalCountData){
                        CallHistoryRequest()
                    }

                }
            }
        })


    }

    private fun PopUpVoicePlay(v: View, id: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = inflater.inflate(R.layout.popup_seekbar, null)
        popupWindow = PopupWindow(
            layout,
            android.app.ActionBar.LayoutParams.MATCH_PARENT,
            android.app.ActionBar.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow!!.contentView = layout
        imgclose = layout.findViewById<View>(R.id.imgclose) as ImageView
        btnDownload = layout.findViewById<View>(R.id.btnDownload) as Button
        webview = layout.findViewById<WebView>(R.id.webviewVoice)
        popupWindow!!.showAtLocation(v, Gravity.CENTER, 0, 0)
        val container = popupWindow!!.contentView.parent as View
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.7f
        wm.updateViewLayout(container, p)
        val progressDialog = CustomLoading.createProgressDialog(this)
        webview!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                progressDialog.show()
                if (progress == 100) {
                    progressDialog.dismiss()
                }
            }
        }
        webview!!.webViewClient = MyWebViewClient(this)
        webview!!.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings = webview!!.settings
        webSettings.loadsImagesAutomatically = true
        webSettings.builtInZoomControls = false
        webSettings.javaScriptEnabled = true
        webview!!.loadUrl(VoiceFilePath!!)
        progressDialog.dismiss()
        imgclose!!.setOnClickListener(
            {
                webview!!.destroy()
                if (mediaPlayer!!.isPlaying)
                    mediaPlayer!!.stop()
                popupWindow!!.dismiss()

            })
    }

    private fun CallHistoryRequest() {
        Log.d("testRequest","test")
        var token = SharedPreference.getSHToken(this)
        var customerID = SharedPreference.getCustomerID(this)
        var agentID = SharedPreference.getAgentID(this)
        dashboardViewmodel!!.geteCallHistoryLiveData(PageLimit,Offset,"",0,customerID!!.toInt(),agentID!!.toInt(),token!!, this)
    }


    fun GetTotalCountCallHistory(){
        Log.d("total","api")
        var token = SharedPreference.getSHToken(this)
        var customerID = SharedPreference.getCustomerID(this)
        var agentID = SharedPreference.getAgentID(this)

        dashboardViewmodel!!.getCallHistoryCount(PageLimit,0,"",1, customerID!!.toInt(),agentID!!.toInt(), token!!, this)
    }
}