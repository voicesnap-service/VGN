package com.vsnap.vgn.application.Activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Adapters.FollowUpDetailsReportAdapter
import com.vsnap.vgn.application.Interface.FollowUpReportListener
import com.vsnap.vgn.application.Modal.GetFollowUpDetailsData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Dashboard
import java.text.SimpleDateFormat
import java.util.*

class FollowUpDetails : AppCompatActivity() {
    var dashboardViewmodel: Dashboard? = null
    var cal = Calendar.getInstance()

    @JvmField
    @BindView(R.id.lblFromdate)
    var lblFromdate: TextView? = null

    @JvmField
    @BindView(R.id.lblTodate)
    var lblTodate: TextView? = null

    var FromDate: String? = null
    var type: String? = null

    var Offset: Int = 0
    var PageLimit: Int = 10

    @JvmField
    @BindView(R.id.rvFollowReport)
    var rvFollowReport: RecyclerView? = null

    @JvmField
    @BindView(R.id.lblNoRecordsFound)
    var lblNoRecordsFound: TextView? = null

    @JvmField
    @BindView(R.id.txtFollowUpSearch)
    var txtFollowUpSearch: EditText? = null

    @JvmField
    @BindView(R.id.idNestedSV)
    var idNestedSV: NestedScrollView? = null

    var followReportAdapter: FollowUpDetailsReportAdapter? = null
    var FollowUpReportData: List<GetFollowUpDetailsData> = ArrayList()

    var Currentdate: String? = null
    var SearchKeyWord: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_up_details)

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

        lblTitle.text = "FollowUp details"

        imgBack.setOnClickListener({
            onBackPressed()
        })
        GetCurrentDate()

        idNestedSV!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
                v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                Offset += 10
                var fromdate = lblFromdate!!.text.toString()
                var todate = lblTodate!!.text.toString()
                if(txtFollowUpSearch!!.text.toString().isNullOrEmpty()){
                    SearchKeyWord=""
                }else{
                    SearchKeyWord = txtFollowUpSearch!!.text.toString()
                }
                Log.d("testScroll","testtd")
                ApiCall(Offset!!,fromdate,todate,SearchKeyWord!!)

            }
        })

        dashboardViewmodel!!.GetUpdateFollowUpMutableLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {

                    FollowUpReportData = response.data!!

                    if (FollowUpReportData.size > 0) {
                        lblNoRecordsFound!!.visibility = View.GONE

                        lblNoRecordsFound!!.text = message
                        rvFollowReport!!.visibility = View.VISIBLE
                        followReportAdapter = FollowUpDetailsReportAdapter(FollowUpReportData!!, this,
                                object : FollowUpReportListener {
                                    override fun onReportClick(
                                        holder: FollowUpDetailsReportAdapter.MyViewHolder,
                                        data: GetFollowUpDetailsData
                                    ) {
                                        holder.btnFollowUpCompleted.setOnClickListener({
                                            FollowUpdateCompleted(data.customer_id)
                                        }
                                        )
                                    }
                                })
                        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                        rvFollowReport!!.layoutManager = mLayoutManager
                        rvFollowReport!!.itemAnimator = DefaultItemAnimator()
                        rvFollowReport!!.adapter = followReportAdapter
                        rvFollowReport!!.recycledViewPool.setMaxRecycledViews(0, 80)
                        followReportAdapter!!.notifyDataSetChanged()
                    } else {
                        lblNoRecordsFound!!.visibility = View.VISIBLE
                        lblNoRecordsFound!!.text = message

                        rvFollowReport!!.visibility = View.GONE
                    }

                } else {
                    lblNoRecordsFound!!.visibility = View.VISIBLE
                    lblNoRecordsFound!!.text = message

                    rvFollowReport!!.visibility = View.GONE
                }
            } else {
                lblNoRecordsFound!!.visibility = View.VISIBLE
                rvFollowReport!!.visibility = View.GONE

            }
        }

        dashboardViewmodel!!.GetFollowUpStatusMutableLiveData!!.observe(this) { response ->
            if (response != null) {
                Log.d("UpdateStatus", "test")
                val status = response.status
                val message = response.message
                if (status == 1) {
                    CommonUtil.AlertFinishOk(this, message)

                } else {
                    CommonUtil.ApiAlert(this, message)
                }

            } else {
                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }

    }

    fun GetCurrentDate() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        val myDate = Date()
        Currentdate = sdf.format(myDate)
        lblFromdate!!.text = Currentdate
        lblTodate!!.text = Currentdate
        ApiCall(Offset, Currentdate!!, Currentdate!!,"")

    }

    @OnClick(R.id.imgsearch)
    fun searchClick() {

        var fromdate = lblFromdate!!.text.toString()
        var todate = lblTodate!!.text.toString()
        if(txtFollowUpSearch!!.text.toString().isNullOrEmpty()){
            SearchKeyWord=""
        }else{
            SearchKeyWord = txtFollowUpSearch!!.text.toString()

        }
        ApiCall(Offset, fromdate!!, todate!!,SearchKeyWord!!)
    }

    @OnClick(R.id.FromDateLayout)
    fun FromDateClick() {
        DatePickerDialog(
            this, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
        type = "0"
    }

    @OnClick(R.id.ToDateLayout)
    fun ToDateClick() {
        DatePickerDialog(
            this, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
        type = "1"

    }

    val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePicker, year: Int, monthOfYear: Int,
            dayOfMonth: Int
        ) {
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            if (type.equals("0")) {
                updateLabel(lblFromdate!!)
            } else {
                updateLabel(lblTodate!!)
            }
        }

        fun updateLabel(txt_date: TextView) {
            val myFormat = "dd/MM/yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            txt_date.text = sdf.format(cal.time)
            FromDate = txt_date.text.toString()

        }
    }

    private fun ApiCall(offset: Int, fromdate: String, todate: String,keyword:String) {

        var token = SharedPreference.getSHToken(this)

        Log.d("FomDate", fromdate)
        dashboardViewmodel!!.getFollowUpDetails(
            PageLimit,
            offset,
            keyword,
            0,
            fromdate!!,
            todate,
            CommonUtil.UserData!!.member_id!!,
            token!!,
            this
        )
    }

    fun FollowUpdateCompleted(customerid: Int) {
        var token = SharedPreference.getSHToken(this)
        val jsonObject = JsonObject()
        jsonObject.addProperty(ApiRequestNames.Req_MemberID, CommonUtil.UserData!!.member_id)
        jsonObject.addProperty(ApiRequestNames.Req_CustomerID, customerid)
        dashboardViewmodel!!.updateFollowUpStatus(jsonObject, token!!, this)
    }


}