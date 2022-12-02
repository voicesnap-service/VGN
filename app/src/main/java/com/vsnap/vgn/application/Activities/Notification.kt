package com.vsnap.vgn.application.Activities

import android.content.Intent
import butterknife.BindView
import android.os.Bundle
import butterknife.ButterKnife
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import com.vsnap.vgn.application.Adapters.NotificationAdapter
import com.vsnap.vgn.application.Modal.NotificationData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Dashboard
import java.util.ArrayList

class Notification : AppCompatActivity() {
    var notificationadapter: NotificationAdapter? = null

    @JvmField
    @BindView(R.id.rvNotification)
    var rvNotification: RecyclerView? = null

    @JvmField
    @BindView(R.id.NestedScroll)
    var NestedScroll: NestedScrollView? = null

    var dashboardViewmodel: Dashboard? = null

    var Offset: Int = 0
    var PageLimit: Int = 10

    var TotalCountData: Int = 0

    var GetNotificationData: ArrayList<NotificationData> = ArrayList()
    var OverAllNotificationList: ArrayList<NotificationData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        ButterKnife.bind(this)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.actionbar_layout)
        val view = supportActionBar!!.customView
        val lblTitle = view.findViewById<View>(R.id.lblTitle) as TextView
        val imgBack = view.findViewById<View>(R.id.imgBack) as ImageView
        val rytAction = view.findViewById<View>(R.id.rytAction) as RelativeLayout

        lblTitle.text = "Notifications"
        dashboardViewmodel = ViewModelProvider(this).get(Dashboard::class.java)
        dashboardViewmodel!!.init()

        imgBack.setOnClickListener({
            onBackPressed()
        })

        TotalCountNotificatonRequest()

        dashboardViewmodel!!.NotificationTotalCountLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                TotalCountData = response.data!!.size
                Log.d("TotalCountData", TotalCountData.toString())
                NotificatonRequest()
            } else {
                NotificatonRequest()
            }
        }


        dashboardViewmodel!!.NotificationLiveData!!.observe(this) { response ->
            if (response != null) {

                val status = response.status
                val message = response.message

                GetNotificationData.clear()
                if (status == 1) {

                    GetNotificationData = response.data!!

                    OverAllNotificationList.addAll(GetNotificationData)

                    Log.d("OverAllListSize",OverAllNotificationList.size.toString())
                    notificationadapter = NotificationAdapter(OverAllNotificationList, this@Notification)
                    val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@Notification)

                    rvNotification!!.layoutManager = mLayoutManager
                    rvNotification!!.itemAnimator = DefaultItemAnimator()
                    rvNotification!!.adapter = notificationadapter
                    rvNotification!!.recycledViewPool.setMaxRecycledViews(0, 80)
                    notificationadapter!!.notifyDataSetChanged()


                } else {
                    CommonUtil.ApiAlert(this@Notification, message)
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
//                    Offset += 10
                    Offset = Offset+PageLimit

                    Log.d("NotificaOffsetSize", Offset.toString())
                    if(Offset < TotalCountData){
                        NotificatonRequest()

                    }

                }
            }
        })

    }

    private fun NotificatonRequest() {
        var token = SharedPreference.getSHToken(this)
        dashboardViewmodel!!.getNotificationList(PageLimit,0,0,0,CommonUtil.UserData!!.member_id,token!!, this)
    }

    private fun TotalCountNotificatonRequest() {
        var token = SharedPreference.getSHToken(this)
        dashboardViewmodel!!.getTotalNotificaionCount(1,CommonUtil.UserData!!.member_id,token!!, this)
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}
