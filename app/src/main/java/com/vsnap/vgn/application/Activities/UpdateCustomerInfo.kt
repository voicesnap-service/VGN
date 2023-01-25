package com.vsnap.vgn.application.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.GetCustomerInfoData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Dashboard
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpdateCustomerInfo : AppCompatActivity() {

    var dashboardViewmodel: Dashboard? = null
    var GetCustomerData: List<GetCustomerInfoData> = ArrayList()

    @JvmField
    @BindView(R.id.txtCustomerName)
    var txtCustomerName: EditText? = null

    @JvmField
    @BindView(R.id.txtMobileNumber)
    var txtMobileNumber: EditText? = null

    @JvmField
    @BindView(R.id.txtEmail)
    var txtEmail: EditText? = null

    @JvmField
    @BindView(R.id.txtAlternateMobileNumber)
    var txtAlternateMobileNumber: EditText? = null

    @JvmField
    @BindView(R.id.txtPinCode)
    var txtPinCode: EditText? = null

    @JvmField
    @BindView(R.id.txtAreaName)
    var txtAreaName: EditText? = null

    @JvmField
    @BindView(R.id.txtFollowUpdate)
    var txtFollowUpdate: TextView? = null

    @JvmField
    @BindView(R.id.txtFollowUpTime)
    var txtFollowUpTime: TextView? = null

    @JvmField
    @BindView(R.id.txtActionTaken)
    var txtActionTaken: EditText? = null


    var CustomerName: String? = null
    var CustomerEmail: String? = null
    var CusMobileNumber: String? = null
    var CusAlterNateNumber: String? = null
    var CusPinCode: String? = null
    var CusAreaName: String? = null
    var CusFollowUpDate: String? = null
    var CusFollowUpTime: String? = null
    var CusActionTaken: String? = null
    var FromDate: String? = null
    val myCalendar = Calendar.getInstance()
    var cal = Calendar.getInstance()
    var mTimePicker: TimePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_customer_info_ui)
        ButterKnife.bind(this)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.actionbar_layout)
        val view = supportActionBar!!.customView
        val lblTitle = view.findViewById<View>(R.id.lblTitle) as TextView
        val imgBack = view.findViewById<View>(R.id.imgBack) as ImageView
        val rytAction = view.findViewById<View>(R.id.rytAction) as RelativeLayout
        lblTitle.text = "View Info"
        imgBack.setOnClickListener({
            onBackPressed()
        })

        dashboardViewmodel = ViewModelProvider(this).get(Dashboard::class.java)
        dashboardViewmodel!!.init()
        var token = SharedPreference.getSHToken(this)
        var customerID = SharedPreference.getCustomerID(this)
        dashboardViewmodel!!.getCustomerInfo(
            customerID!!.toInt(),
            CommonUtil.UserData!!.member_id,
            token!!,
            this
        )

        dashboardViewmodel!!.CustomerInfoLiveData!!.observe(this) { response ->
            if (response != null) {

                Log.d("notification", "test")
                val status = response.status
                val message = response.message
                if (status == 1) {
                    GetCustomerData = response.data!!
                    SetCustomerData()
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.AlertFinishOk(this, "Something went wrong")
            }
        }

        dashboardViewmodel!!.UpdateCustomerInfoLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    CommonUtil.AlertFinishOk(this, message)
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.AlertFinishOk(this, "Something went wrong")
            }
        }
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                txtFollowUpTime!!.setText(String.format("%d : %d", hourOfDay, minute))
            }
        }, hour, minute, false)


        val cal = Calendar.getInstance()


    }

    private fun SetCustomerData() {

        txtCustomerName!!.setText(GetCustomerData.get(0).customer_name)
        txtMobileNumber!!.setText(GetCustomerData.get(0).customer_mobile)
        txtAlternateMobileNumber!!.setText(GetCustomerData.get(0).customer_alternate_mobile)
        txtEmail!!.setText(GetCustomerData.get(0).customer_email)
        txtPinCode!!.setText(GetCustomerData.get(0).customer_pincode)
        txtAreaName!!.setText(GetCustomerData.get(0).customer_area)
        txtFollowUpdate!!.setText(GetCustomerData.get(0).follow_up_date)
        txtFollowUpTime!!.setText(GetCustomerData.get(0).follow_up_time)
        txtActionTaken!!.setText(GetCustomerData.get(0).action_taken)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    @OnClick(R.id.btnCancel)
    fun canclclick() {

        Log.d("btnCancel", "btnCancel")
        onBackPressed()

    }

    @OnClick(R.id.txtFollowUpTime)
    fun FollowupTimeClick() {
        val cal = Calendar.getInstance()


        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            txtFollowUpTime!!.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        ).show()
    }

    @OnClick(R.id.txtFollowUpdate)
    fun FollowTimeClick() {


        DatePickerDialog(
            this, dateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePicker, year: Int, monthOfYear: Int,
            dayOfMonth: Int
        ) {
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(txtFollowUpdate!!)

        }
    }

    fun updateLabel(txt_date: TextView) {

        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txt_date.text = sdf.format(cal.time)

        FromDate = txt_date.text.toString()

    }

    @OnClick(R.id.btnUpdate)
    fun UpdateClick() {
        CustomerName = txtCustomerName!!.text.toString()
        CustomerEmail = txtEmail!!.text.toString()
        CusMobileNumber = txtMobileNumber!!.text.toString()
        CusAlterNateNumber = txtAlternateMobileNumber!!.text.toString()
        CusPinCode = txtPinCode!!.text.toString()
        CusAreaName = txtAreaName!!.text.toString()
        CusFollowUpDate = txtFollowUpdate!!.text.toString()
        CusFollowUpTime = txtFollowUpTime!!.text.toString()
        CusActionTaken = txtActionTaken!!.text.toString()

//
//        if (CustomerName.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer name")
//
//        } else if (CustomerEmail.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer email")
//
//        } else if (CusMobileNumber.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer mobilenumber")
//
//        } else if (CusAlterNateNumber.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer Alternate mobilenumber")
//
//        } else if (CusPinCode.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer Pincode")
//
//        } else if (CusAreaName.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer Area")
//        } else if (CusFollowUpDate.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer followup date")
//        } else if (CusFollowUpTime.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer followup time")
//        } else if (CusActionTaken.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, "Kindly enter customer Action taken")
//        }
        if (CusMobileNumber.isNullOrEmpty()) {
            CommonUtil.CommonAlertOk(this, "Kindly enter mobilenumber")

        } else {
            AlertMeeting("Are you sure do you want to this update customer info")
        }

    }

    fun AlertMeeting(message: String?) {
        val textView = TextView(this)
        textView.text = "Confirm"
        textView.setPadding(20, 30, 20, 30)
        textView.textSize = 20f
        textView.setBackgroundColor(Color.parseColor(resources.getString(R.string.txt_Color_alert)))
        textView.setTextColor(Color.WHITE)
        val builder = AlertDialog.Builder(this)
        builder.setCustomTitle(textView)
        builder.setCancelable(false)
        builder.setMessage(message)
        builder.setPositiveButton(
            resources.getString(R.string.txt_Yes)
        ) { dialog, which ->
            UpdateCustomerApi()
        }
        builder.setNegativeButton(
            resources.getString(R.string.txt_No)
        ) { dialog, which -> builder.setCancelable(false) }
        builder.create().show()
    }

    private fun UpdateCustomerApi() {

        var customerid = SharedPreference.getCustomerID(this)
        val jsonObject = JsonObject()
        jsonObject.addProperty(ApiRequestNames.Req_LoginId, CommonUtil.UserData!!.member_id)
        jsonObject.addProperty(ApiRequestNames.Req_CustomerID, customerid)
        jsonObject.addProperty(ApiRequestNames.Req_CustomerName, CustomerName)
        jsonObject.addProperty(ApiRequestNames.Req_CustomerEmail, CustomerEmail)
        jsonObject.addProperty(ApiRequestNames.Req_CusMobilenumber, CusMobileNumber)
        jsonObject.addProperty(ApiRequestNames.Req_CusAlternateMobilenumber, CusAlterNateNumber)
        jsonObject.addProperty(ApiRequestNames.Req_CusPinCode, CusPinCode)
        jsonObject.addProperty(ApiRequestNames.Req_CusArea, CusAreaName)
        jsonObject.addProperty(ApiRequestNames.Req_CusFollowUpdate, CusFollowUpDate)
        jsonObject.addProperty(ApiRequestNames.Req_CusFollowUptime, CusFollowUpTime)
        jsonObject.addProperty(ApiRequestNames.Req_ActionTaken, CusActionTaken)

        var token = SharedPreference.getSHToken(this)

        dashboardViewmodel!!.updateCustomerInfo(jsonObject, token!!, this)

    }


}