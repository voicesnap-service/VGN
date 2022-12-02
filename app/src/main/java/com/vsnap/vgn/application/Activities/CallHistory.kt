package com.vsnap.vgn.application.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.vsnap.vgn.application.Adapters.CallHistoryAdapter
import com.vsnap.vgn.application.Modal.CallHistoryData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Utils.CommonUtil
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

                    notificationadapter = CallHistoryAdapter(GetCallHistoryLiveData, this)
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