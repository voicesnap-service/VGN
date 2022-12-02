package com.vsnap.vgn.application.Activities

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Adapters.RecentCallAdapter
import com.vsnap.vgn.application.Interface.RecentCardClickListener
import com.vsnap.vgn.application.Modal.RecentCallsData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.*
import com.vsnap.vgn.application.Utils.CommonUtil.UserData
import com.vsnap.vgn.application.ViewModel.Dashboard
import java.util.*


class DashboardHome : AppCompatActivity() {
    @JvmField
    @BindView(R.id.imgHome)
    var imgHome: ImageView? = null

    @JvmField
    @BindView(R.id.imgNotiifcation)
    var imgNotiifcation: ImageView? = null

    @JvmField
    @BindView(R.id.imghelp)
    var imghelp: ImageView? = null

    @JvmField
    @BindView(R.id.imgSettings)
    var imgSettings: ImageView? = null

    @JvmField
    @BindView(R.id.lblHome)
    var lblHome: TextView? = null

    @JvmField
    @BindView(R.id.lblNotifications)
    var lblNotifications: TextView? = null

    @JvmField
    @BindView(R.id.lblhelp)
    var lblhelp: TextView? = null

    @JvmField
    @BindView(R.id.lblSetting)
    var lblSetting: TextView? = null

    @JvmField
    @BindView(R.id.imgRecentCalls)
    var imgRecentCalls: ImageView? = null

    @JvmField
    @BindView(R.id.imgMissedCalls)
    var imgMissedCalls: ImageView? = null

    @JvmField
    @BindView(R.id.lblMissedCallls)
    var lblMissedCallls: TextView? = null

    @JvmField
    @BindView(R.id.lblRecentCalls)
    var lblRecentCalls: TextView? = null

    @JvmField
    @BindView(R.id.rytMenuIcon)
    var rytMenuIcon: RelativeLayout? = null

    @JvmField
    @BindView(R.id.NestedScroll)
    var NestedScroll: NestedScrollView? = null

    @JvmField
    @BindView(R.id.recyclerCustomer)
    var recyclerCustomer: RecyclerView? = null

    @JvmField
    @BindView(R.id.lblNoRecordsFound)
    var lblNoRecordsFound: TextView? = null

    @JvmField
    @BindView(R.id.imgMenu)
    var imgMenu: ImageView? = null

    @JvmField
    @BindView(R.id.framelayout)
    var FrameLayout1: FrameLayout? = null

    @JvmField
    @BindView(R.id.layoutSearch)
    var layoutSearch: ConstraintLayout? = null

    @JvmField
    @BindView(R.id.imgsearch)
    var imgsearch: ImageView? = null

    @JvmField
    @BindView(R.id.txtFollowUpSearch)
    var txtSearch: EditText? = null

    var deviceToken = ""
    var dashboardViewmodel: Dashboard? = null
    var recentCallAdapter: RecentCallAdapter? = null
    var webview: WebView? = null
    var type: String? = null
    var RecentCallListdata: ArrayList<RecentCallsData> = ArrayList()
    var OverAllCallList: ArrayList<RecentCallsData> = ArrayList()
    var ClickMenu = true
    var Offset: Int = 0
    var PageLimit: Int = 4
    var TotalCountData: Int = 0
    var SearchKeyWord: String? = null
    var mediaPlayer: MediaPlayer? = MediaPlayer()
    var mediaFileLengthInMilliseconds = 0
    var handler = Handler()
    var iMediaDuration = 0
    var VoiceFilePath: String? = null
    private val VOICE_FOLDER: String? = "Smart Call/Voice/"
    var voicepath: String? = null
    var mHandler = Handler()
    var runnable: Runnable? = null
    var Loader = 0
    var msgcontent: String? = null
    var path: String? = null
    var imgclose: ImageView? = null
    var imgPlaypause: ImageView? = null
    var seekbarvoice: SeekBar? = null
    var lblduration: TextView? = null
    var voiceLoader: ProgressBar? = null
    var btnDownload: Button? = null
    var popupWindow: PopupWindow? = null
    var popupBlockWindow: PopupWindow? = null
    var Filepath: String? = ""
    var UserLogin = false
    var CallType = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dahboard_home)
        ButterKnife.bind(this)

        dashboardViewmodel = ViewModelProvider(this).get(Dashboard::class.java)
        dashboardViewmodel!!.init()
        type = "recent"

        var emailid = SharedPreference.getShH_Email(this)
        FirebaseMessaging.getInstance().setAutoInitEnabled(true)
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Fetching FCM ", task.exception.toString())
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                deviceToken = task.result
                Log.d("FCM TOKEN", deviceToken)
                val jsonObject = JsonObject()
                jsonObject.addProperty(ApiRequestNames.Req_EmailID, emailid)
                jsonObject.addProperty(ApiRequestNames.Req_DeviceType, CommonUtil.DeviceType)
                jsonObject.addProperty(ApiRequestNames.Req_DeviceToken, deviceToken)
                Log.d("deviceToken", deviceToken)
                dashboardViewmodel!!.updateDeviceToken(jsonObject, this)
            })
        dashboardViewmodel!!.DeviceTokenMutableLiveData!!.observe(this) { response ->
            if (response != null) {
                GetTotalCountApi()
                val status = response.status
                val message = response.message
                Log.d("Devicetoken", message!!)
            } else {
                GetTotalCountApi()
            }
        }
        dashboardViewmodel!!.GetCallListCountLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                TotalCountData = response.data!!.size
                Log.d("TotalCountData",TotalCountData.toString())
                ApiCall(Offset)
            }
            else {
                ApiCall(Offset)
            }
        }
        dashboardViewmodel!!.RecentCallsLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message

                if (status == 1) {
                    Log.d("CallType", CallType.toString())
                    RecentCallListdata.clear()
                    if(CallType){
                        RecentCallListdata = response.data!!

                        if(Offset == 0){
                            OverAllCallList.clear();
                            OverAllCallList.addAll(RecentCallListdata)
                        }
                        else{
                            OverAllCallList.addAll(RecentCallListdata)
                        }

                        if(OverAllCallList.size > 0){
                            lblNoRecordsFound!!.visibility = View.GONE
                            recyclerCustomer!!.visibility = View.VISIBLE
                        }
                        else{
                            lblNoRecordsFound!!.visibility = View.VISIBLE
                            recyclerCustomer!!.visibility = View.GONE
                        }

                        Log.d("OverAllCAllList2",OverAllCallList.size.toString())
                        recentCallAdapter = RecentCallAdapter(OverAllCallList,"Recent", this,
                            object : RecentCardClickListener {
                                override fun onClick(
                                    holder: RecentCallAdapter.MyViewHolder,
                                    data: RecentCallsData
                                ) {
                                    SharedPreference.putCustomerDetails(
                                        this@DashboardHome,
                                        data.customer_id.toString(),
                                        data.agent_id.toString()
                                    )
                                    Log.d("Conf_id", data.conference_id.toString())
                                    holder.rytCallHistory.setOnClickListener({
                                        val intent = Intent(this@DashboardHome, CallHistory::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                    })

                                    holder.rytUpdateInfo.setOnClickListener({
                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(),
                                            data.agent_id!!
                                        )

                                        val intent =
                                            Intent(this@DashboardHome, UpdateCustomerInfo::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)

                                    })

                                    holder.rytShare.setOnClickListener({
                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(),
                                            data.agent_id!!
                                        )

                                        Log.d("Conf_id", data.conference_id.toString())


                                        val intent = Intent(this@DashboardHome, ShareDocuments::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        intent.putExtra("Projectid",data.project_id)
                                        intent.putExtra("CustomerID",data.customer_id)
                                        intent.putExtra("ConfID",data.conference_id)

                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                    })

                                    holder.rytCallCustomer.setOnClickListener({
                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(),
                                            data.agent_id!!
                                        )
                                        ClickToCall(data)
                                    })
                                    holder.rytBlock.setOnClickListener({

                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(),
                                            data.agent_id!!
                                        )
                                        PopUpForBlocking(it)

                                    })

                                    holder.rytrecording.setOnClickListener({
                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(), data.agent_id!!
                                        )
                                        var id = data.customer_id

                                        VoiceFilePath = data.file_recording
                                        if (VoiceFilePath.isNullOrEmpty()) {
                                            AlertFinishOk("No Voice File")
                                        } else {
                                            PopUpVoicePlay(it, id.toString())
                                        }

                                    })
                                }
                            })
                        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                        recyclerCustomer!!.layoutManager = mLayoutManager
                        recyclerCustomer!!.itemAnimator = DefaultItemAnimator()
                        recyclerCustomer!!.adapter = recentCallAdapter
                        recentCallAdapter!!.notifyDataSetChanged()

                    }
                    else {
                        Log.d("Missed","Missed")
                        RecentCallListdata = response.data!!

                        if(Offset == 0){
                            OverAllCallList.clear();
                            OverAllCallList.addAll(RecentCallListdata)
                        }
                        else{
                            OverAllCallList.addAll(RecentCallListdata)
                        }

                        Log.d("OverAllCallList_size",OverAllCallList.size.toString())

                        if(OverAllCallList.size > 0){
                            lblNoRecordsFound!!.visibility = View.GONE
                            recyclerCustomer!!.visibility = View.VISIBLE
                        }
                        else{
                            lblNoRecordsFound!!.visibility = View.VISIBLE
                            recyclerCustomer!!.visibility = View.GONE
                        }

                        recentCallAdapter = RecentCallAdapter(OverAllCallList,"Missed", this,
                            object : RecentCardClickListener {
                                override fun onClick(
                                    holder: RecentCallAdapter.MyViewHolder,
                                    data: RecentCallsData
                                ) {
                                    SharedPreference.putCustomerDetails(
                                        this@DashboardHome,
                                        data.customer_id.toString(),
                                        data.agent_id.toString()
                                    )
                                    holder.rytCallHistory.setOnClickListener({
                                        val intent =
                                            Intent(this@DashboardHome, CallHistory::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                    })

                                    holder.rytUpdateInfo.setOnClickListener({
                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(),
                                            data.agent_id!!
                                        )

                                        val intent =
                                            Intent(
                                                this@DashboardHome,
                                                UpdateCustomerInfo::class.java
                                            )
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)

                                    })

                                    holder.rytShare.setOnClickListener({
                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(),
                                            data.agent_id!!
                                        )

                                        val intent =
                                            Intent(this@DashboardHome, ShareDocuments::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        intent.putExtra("Projectid", data.project_id)
                                        intent.putExtra("CustomerID", data.customer_id)
                                        intent.putExtra("ConfID", data.conference_id)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                    })

                                    holder.rytCallCustomer.setOnClickListener({
                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(),
                                            data.agent_id!!
                                        )
                                        ClickToCall(data)
                                    })
                                    holder.rytBlock.setOnClickListener({

                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(),
                                            data.agent_id!!
                                        )

                                        PopUpForBlocking(it)

                                    })

                                    holder.rytrecording.setOnClickListener({
                                        SharedPreference.putCustomerDetails(
                                            this@DashboardHome,
                                            data.customer_id.toString(), data.agent_id!!
                                        )
                                        var id = data.customer_id

                                        VoiceFilePath = data.file_recording
                                        if (VoiceFilePath.isNullOrEmpty()) {
                                            AlertFinishOk("No Voice File")
                                        } else {
                                            PopUpVoicePlay(it, id.toString())

                                        }

                                    })
                                }
                            })
                        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                        recyclerCustomer!!.layoutManager = mLayoutManager
                        recyclerCustomer!!.itemAnimator = DefaultItemAnimator()
                        recyclerCustomer!!.adapter = recentCallAdapter
                        recentCallAdapter!!.notifyDataSetChanged()
                    }

                } else {
                    if(Offset == 0) {
                        lblNoRecordsFound!!.visibility = View.VISIBLE
                        recyclerCustomer!!.visibility = View.GONE
                    }
                }
            } else {
                CommonUtil.ApiAlert(this, "Something went wrong")

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
                    Offset = Offset+PageLimit

                    Log.d("OffsetSize", Offset.toString())
                    if(Offset < TotalCountData){
                        ApiCall(Offset)
                    }

                }
            }
        })

        dashboardViewmodel!!.BlockCustomerCustomerLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    Alert(this, message, true)
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.ApiAlert(this, "Something went wrong")

            }
        }
        dashboardViewmodel!!.CallCustomerLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    CommonUtil.ApiAlert(this, message)
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }
    }


    private fun PopUpForBlocking(v: View) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = inflater.inflate(R.layout.popup_blocking, null)
        popupBlockWindow = PopupWindow(
            layout,
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
            true
        )
        popupBlockWindow!!.contentView = layout
        val edReason = layout.findViewById<View>(R.id.edReason) as EditText
        val btnBlock = layout.findViewById<View>(R.id.btnBlock) as Button
        val imgclose = layout.findViewById<View>(R.id.imgclose) as ImageView

        popupBlockWindow!!.showAtLocation(v, Gravity.CENTER, 0, 0)
        val container =
            popupBlockWindow!!.contentView.parent as View
        val wm =
            getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p =
            container.layoutParams as WindowManager.LayoutParams
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.7f
        wm.updateViewLayout(container, p)
        btnBlock.setOnClickListener {

            if (edReason.text.toString().equals("")) {
                CommonUtil.CommonAlertOk(this, "Enter reason to block a customer")
            } else {
                Apiblock()
            }
        }
        imgclose.setOnClickListener({
            popupBlockWindow!!.dismiss()
        })

    }

    private fun Apiblock() {
        var customerid = SharedPreference.getCustomerID(this)
        var token = SharedPreference.getSHToken(this)
        val jsonObject = JsonObject()
        jsonObject.addProperty(ApiRequestNames.Req_LoginId, CommonUtil.UserData!!.member_id)
        jsonObject.addProperty(ApiRequestNames.Req_CustomerID, customerid)
        dashboardViewmodel!!.blockCustomer(jsonObject,token!!, this)
    }

    private fun ClickToCall(data: RecentCallsData) {
        var token = SharedPreference.getSHToken(this)

        val jsonObject = JsonObject()
        jsonObject.addProperty(ApiRequestNames.Req_AgentID, data.agent_id)
        jsonObject.addProperty(ApiRequestNames.Req_AgentNumber, UserData!!.mobile_number)
        jsonObject.addProperty(ApiRequestNames.Req_CustomerID, data.customer_id)
        jsonObject.addProperty(ApiRequestNames.Req_CustomerNumber, data.cusomer_mobile)
        jsonObject.addProperty(ApiRequestNames.Req_OldConferenceID, data.conference_id)
        jsonObject.addProperty(ApiRequestNames.Req_OrganisationID, UserData!!.organisation_id)
        jsonObject.addProperty(ApiRequestNames.Req_ProjectID, data.project_id)
        dashboardViewmodel!!.callCustomer(jsonObject, token!!, this)
    }


    fun AlertFinishOk(msg: String?) {
        if (applicationContext != null) {
            val dlg = AlertDialog.Builder(applicationContext)
            dlg.setTitle("Info")
            dlg.setMessage(msg)
            dlg.setPositiveButton("OK") { dialog, which ->

            }
            dlg.setCancelable(false)
            dlg.create()
            dlg.show()
        }
    }


    private fun PopUpVoicePlay(v: View, id: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = inflater.inflate(R.layout.popup_seekbar, null)
        popupWindow = PopupWindow(
            layout,
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT,
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

    fun Alert(activity: Activity?, msg: String?, value: Boolean) {
        if (activity != null) {
            val dlg = AlertDialog.Builder(activity)
            dlg.setTitle("Info")
            dlg.setMessage(msg)
            dlg.setPositiveButton("OK") { dialog, which ->
                if (value) {
                    popupBlockWindow!!.dismiss()
                } else {
                    popupWindow!!.dismiss()
                }
            }
            dlg.setCancelable(false)
            dlg.create()
            dlg.show()
        }
    }

    private fun ApiCall(offset: Int) {
        var token = SharedPreference.getSHToken(this)
        if (txtSearch!!.text.toString().isNullOrEmpty()) {
            SearchKeyWord = ""
        } else {
            SearchKeyWord = txtSearch!!.text.toString()
        }

        dashboardViewmodel!!.getCallsbytype(
            PageLimit,
            offset,
            SearchKeyWord!!,
            type!!,
            token!!,
            0,
            UserData!!.member_id,
            this
        )
    }

    private fun GetTotalCountApi() {
        var token = SharedPreference.getSHToken(this)
        dashboardViewmodel!!.getCallTotalCount(
            1,
            type!!,
            CommonUtil.UserData!!.member_id,
            token!!,
            this
        )
    }

    @OnClick(R.id.LayoutRecentCalls)
    fun RecentCallClick() {



        Offset = 0
        imgRecentCalls!!.setImageResource(R.drawable.recentcall_pink)
        lblRecentCalls!!.setTextColor(Color.parseColor("#d93b74"))
        imgMissedCalls!!.setImageResource(R.drawable.missedcall_grey)
        lblMissedCallls!!.setTextColor(Color.parseColor("#9b9b9b"))
        type = "recent"
        CallType =true
        GetTotalCountApi()

//        ApiCall(Offset)



    }

    @OnClick(R.id.imgMenu)
    fun SideMenuClick(view: View) {
        popFollowUpmenu(view)

    }

    @OnClick(R.id.LayoutMissedCalls)
    fun MissedCallClick() {

        Offset = 0

        imgRecentCalls!!.setImageResource(R.drawable.recentcall_grey)
        lblRecentCalls!!.setTextColor(Color.parseColor("#9b9b9b"))
        imgMissedCalls!!.setImageResource(R.drawable.missedcall_pink)
        lblMissedCallls!!.setTextColor(Color.parseColor("#d93b74"))
        type = "missed"
        CallType = false
        GetTotalCountApi()

//        ApiCall(Offset)
        recyclerCustomer!!.scrollToPosition(0)
        recentCallAdapter!!.notifyDataSetChanged()
    }

    @OnClick(R.id.rytHome)
    fun homeclick() {

        imgHome!!.setImageResource(R.drawable.ic_home)
        lblHome!!.setTextColor(Color.parseColor("#414545"))
        imgNotiifcation!!.setImageResource(R.drawable.ic_notification_grey)
        lblNotifications!!.setTextColor(Color.parseColor("#9b9b9b"))
        imghelp!!.setImageResource(R.drawable.ic_help_grey)
        lblhelp!!.setTextColor(Color.parseColor("#9b9b9b"))
        imgSettings!!.setImageResource(R.drawable.ic_settings_grey)
        lblSetting!!.setTextColor(Color.parseColor("#9b9b9b"))
    }

    @OnClick(R.id.rytNotifications)
    fun notificationClick() {
        imgNotiifcation!!.setImageResource(R.drawable.ic_notification)
        lblNotifications!!.setTextColor(Color.parseColor("#414545"))
        imgHome!!.setImageResource(R.drawable.ic_home_grey)
        lblHome!!.setTextColor(Color.parseColor("#9b9b9b"))
        imghelp!!.setImageResource(R.drawable.ic_help_grey)
        lblhelp!!.setTextColor(Color.parseColor("#9b9b9b"))
        imgSettings!!.setImageResource(R.drawable.ic_settings_grey)
        lblSetting!!.setTextColor(Color.parseColor("#9b9b9b"))
        val i = Intent(this, Notification::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

    @OnClick(R.id.rytHelp)
    fun helpClcik() {

        imghelp!!.setImageResource(R.drawable.ic_help)
        lblhelp!!.setTextColor(Color.parseColor("#414545"))
        imgNotiifcation!!.setImageResource(R.drawable.ic_notification_grey)
        lblNotifications!!.setTextColor(Color.parseColor("#9b9b9b"))
        imgHome!!.setImageResource(R.drawable.ic_home_grey)
        lblHome!!.setTextColor(Color.parseColor("#9b9b9b"))
        imgSettings!!.setImageResource(R.drawable.ic_settings_grey)
        lblSetting!!.setTextColor(Color.parseColor("#9b9b9b"))
        CommonUtil.WebUrload(this, CommonUtil.HelpUrl!!)
    }

    @OnClick(R.id.rytSettings)
    fun SettingClick(view: View) {
        imgSettings!!.setImageResource(R.drawable.ic_settings)
        lblSetting!!.setTextColor(Color.parseColor("#414545"))
        imghelp!!.setImageResource(R.drawable.ic_help_grey)
        lblhelp!!.setTextColor(Color.parseColor("#9b9b9b"))
        imgNotiifcation!!.setImageResource(R.drawable.ic_notification_grey)
        lblNotifications!!.setTextColor(Color.parseColor("#9b9b9b"))
        imgHome!!.setImageResource(R.drawable.ic_home_grey)
        lblHome!!.setTextColor(Color.parseColor("#9b9b9b"))

        popupMenuClick(view)

    }

    @OnClick(R.id.imgCollegeLogo)
    fun ProfileClick(view: View) {
        val i = Intent(this, Profile::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

    @OnClick(R.id.imgsearch)
    fun searchClick() {
        var token = SharedPreference.getSHToken(this)

        if(txtSearch!!.text.toString().isNullOrEmpty()){
            SearchKeyWord=""
        }else{
            SearchKeyWord = txtSearch!!.text.toString()

        }

        if(!SearchKeyWord.isNullOrEmpty()){
            if(CallType){
                dashboardViewmodel!!.getCallsbytype(PageLimit, Offset,SearchKeyWord!!,"recent", token!!, 0,
                    UserData!!.member_id ,this)
            }else{
                dashboardViewmodel!!.getCallsbytype(PageLimit, Offset,SearchKeyWord!!,"missed", token!!, 0,
                    UserData!!.member_id ,this)
            }

        }


    }

    private fun popupMenuClick(view: View) {
        val popup = PopupMenu(applicationContext, view)
        popup.menuInflater.inflate(R.menu.settings_menu, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.logout) {
                alert("Are you sure? Do you want to logout", "Logout")
            }
            if (item.itemId == R.id.changepassword) {
                alert("Are you sure? Do you want to change password", "ChangePassword")

            }
            true
        }
    }


    private fun popFollowUpmenu(view: View) {
        val popup = PopupMenu(applicationContext, view)
        popup.menuInflater.inflate(R.menu.folow_up, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.FollowUp) {
                val intent = Intent(this, FollowUpDetails::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            true
        }
    }

    override fun onBackPressed() {
        alert("Are you Sure! Do You Want to Exit From the App ?", "exist")
    }

    fun alert(title: String?, text: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, which ->
            if (text == "Logout") {
                SharedPreference.clearShLogin(this)
                val i = Intent(this, EnterEmail::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                finishAffinity()
            } else if (text == "ChangePassword") {
                val i = Intent(this, ChangePassword::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
            } else if (text == "exist") {
                finishAffinity()
            }
        }
        builder.setNegativeButton(
            "No"
        ) { dialog, which -> builder.setCancelable(false) }
        builder.create().show()
    }

    fun milliSecondsToTimer(milliseconds: Long): String {
        var finalTimerString = ""
        var secondsString = ""
        var minutesString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }

        // Prepending 0 to Minutes if it is one digit
        if (minutes < 10) {
            minutesString = "0$minutes"
        } else {
            minutesString = "" + minutes
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0$seconds"
        } else {
            secondsString = "" + seconds
        }
        finalTimerString = "$finalTimerString$minutesString:$secondsString"

        // return timer string
        return finalTimerString
    }


    private fun primarySeekBarProgressUpdater(fileLength: Int) {
        val iProgress = (mediaPlayer!!.currentPosition.toFloat() / fileLength * 100).toInt()
        seekbarvoice!!.setProgress(iProgress)
        if (mediaPlayer!!.isPlaying) {
            val notification = Runnable {
                lblduration!!.setText(milliSecondsToTimer(mediaPlayer!!.currentPosition.toLong()))
                primarySeekBarProgressUpdater(fileLength)
            }
            handler.postDelayed(notification, 1000)
        }
    }


//    override fun onPause() {
//        super.onPause()
//        webview!!.onPause()
//    }

}