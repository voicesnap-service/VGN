package com.vsnap.vgn.application.Activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
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
import com.vsnap.vgn.application.Adapters.CustomerDetailsAdapter
import com.vsnap.vgn.application.Interface.CustomerCardClickListener
import com.vsnap.vgn.application.Modal.CustomerData
import com.vsnap.vgn.application.Modal.CustomerTypeData
import com.vsnap.vgn.application.Modal.DialNumberDetails
import com.vsnap.vgn.application.Modal.TempType
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.CustomLoading
import com.vsnap.vgn.application.Utils.MyWebViewClient
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Dashboard
import java.text.SimpleDateFormat
import java.util.*

class CustomerInfo : AppCompatActivity() {

    @JvmField
    @BindView(R.id.rvCustomerInfoList)
    var rvCustomerInfoList: RecyclerView? = null

    @JvmField
    @BindView(R.id.lblNoRecordsFound)
    var lblNoRecordsFound: TextView? = null

    var customerAdapter: CustomerDetailsAdapter? = null

    @JvmField
    @BindView(R.id.NestedScroll)
    var NestedScroll: NestedScrollView? = null

    var dashboardViewmodel: Dashboard? = null

    var Offset: Int = 0
    var PageLimit: Int = 10
    var TotalCountData: Int = 0

    var CustomerDataList: ArrayList<CustomerData> = ArrayList()
    var OverAllCallList: ArrayList<CustomerData> = ArrayList()
    var customer_id_to_call: Int = 0
    var NumbersDetailsList: ArrayList<DialNumberDetails> = ArrayList()

    @JvmField
    @BindView(R.id.spinner)
    var spinners: Spinner? = null

    @JvmField
    @BindView(R.id.txtFollowUpSearch)
    var txtFollowUpSearch: EditText? = null

    @JvmField
    @BindView(R.id.lblFromDate)
    var lblFromDate: TextView? = null

    @JvmField
    @BindView(R.id.lblToDate)
    var lblToDate: TextView? = null

    @JvmField
    @BindView(R.id.btnSearch)
    var btnSearch: TextView? = null

    @JvmField
    @BindView(R.id.lnrFromDate)
    var lnrFromDate: LinearLayout? = null


    @JvmField
    @BindView(R.id.lnrToDate)
    var lnrToDate: LinearLayout? = null


    var customer_type: String? = ""
    var CustomerTypeList: List<CustomerTypeData> = ArrayList()

    var VoiceFilePath: String? = null
    var imgclose: ImageView? = null
    var popupWindow: PopupWindow? = null
    var webview: WebView? = null
    var btnDownload: Button? = null
    var mediaPlayer: MediaPlayer? = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_info)
        ButterKnife.bind(this)

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val current = formatter.format(time)
        lblFromDate!!.setText(current)
        lblToDate!!.setText(current)


        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.actionbar_layout)
        val view = supportActionBar!!.customView
        val lblTitle = view.findViewById<View>(R.id.lblTitle) as TextView
        val imgBack = view.findViewById<View>(R.id.imgBack) as ImageView
        val rytAction = view.findViewById<View>(R.id.rytAction) as RelativeLayout
        lblTitle.text = "Customer Info"
        dashboardViewmodel = ViewModelProvider(this).get(Dashboard::class.java)
        dashboardViewmodel!!.init()

        imgBack.setOnClickListener({
            onBackPressed()
        })

        var token = SharedPreference.getSHToken(this)

        dashboardViewmodel!!.getCustomerTypes(this, token!!)
        dashboardViewmodel!!.CustomerTypesLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    CustomerTypeList = response.data!!

                    val templates = arrayOfNulls<String>(CustomerTypeList.size);
                    for (data in CustomerTypeList) {
                        templates[CustomerTypeList.indexOf(data)] = data.unnest
                    }
                    if (spinners != null) {
                        val adapter = ArrayAdapter(
                            this,
                            R.layout.text_spinner, R.id.text1, templates
                        )

                        spinners!!.adapter = adapter

                        spinners!!.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                            ) {

                                customer_type = CustomerTypeList.get(position).unnest!!

                                Offset = 0
                                PageLimit = 10
                                TotalCountData = 0

                                getTotalCount()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }
                        Log.d("TemplateTypeSize", CustomerTypeList.size.toString())
                    }


                } else {
                    CommonUtil.ApiAlert(this, "Something went wrong")
                }
            }
        }

        dashboardViewmodel!!.GetCallListCountLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                TotalCountData = response.data!!.size
                Log.d("TotalCountData", TotalCountData.toString())
                getCustomerDetails()

            } else {
                getCustomerDetails()

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


        dashboardViewmodel!!.DialNumbersLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                var token = SharedPreference.getSHToken(this)
                Log.d("DialNumbers", response.data!!.toString())

                NumbersDetailsList.clear()
                NumbersDetailsList = response.data!!

                if (status == 1) {
                    val jsonObject = JsonObject()
                    jsonObject.addProperty(ApiRequestNames.Req_CustomerID, customer_id_to_call)
                    jsonObject.addProperty(
                        ApiRequestNames.Req_CustomerNumber,
                        NumbersDetailsList[0].customer_number
                    )

                    Log.d("call_request", jsonObject.toString())
                    dashboardViewmodel!!.callCustomer(jsonObject, token!!, this)
                } else if (status == 2) {
                    val number1 = NumbersDetailsList[0].customer_number
                    val number2 = NumbersDetailsList[0].alternate_number1
                    customPopupNumbers(number1!!, number2!!)

                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }

        dashboardViewmodel!!.CustomerDetailsLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message

                if (status == 1) {
                    CustomerDataList.clear()
                    CustomerDataList = response.data!!

                    if (Offset == 0) {
                        OverAllCallList.clear();
                        OverAllCallList.addAll(CustomerDataList)
                    } else {
                        OverAllCallList.addAll(CustomerDataList)
                    }

                    if (OverAllCallList.size > 0) {
                        lblNoRecordsFound!!.visibility = View.GONE
                        rvCustomerInfoList!!.visibility = View.VISIBLE
                    } else {
                        lblNoRecordsFound!!.visibility = View.VISIBLE
                        rvCustomerInfoList!!.visibility = View.GONE
                    }

                    Log.d("OverAllCAllList2", OverAllCallList.size.toString())
                    customerAdapter = CustomerDetailsAdapter(OverAllCallList, "", this,
                        object : CustomerCardClickListener {
                            override fun onClick(
                                holder: CustomerDetailsAdapter.MyViewHolder,
                                data: CustomerData
                            ) {

                                holder.rytCallCustomer.setOnClickListener({
                                    customer_id_to_call = data.customer_id
                                    var token =
                                        SharedPreference.getSHToken(this@CustomerInfo)
                                    dashboardViewmodel!!.getDialNumbers(
                                        this@CustomerInfo,
                                        token!!,
                                        data.customer_id
                                    )
                                })

                                holder.rytUpdateInfo.setOnClickListener({
                                    var id = data.customer_id
                                    VoiceFilePath = data.file_recording
                                    if (VoiceFilePath.isNullOrEmpty()) {
                                        Toast.makeText(
                                            applicationContext,
                                            "No voice file",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        PopUpVoicePlay(it, id.toString())
                                    }

                                })

                            }
                        })
                    val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                    rvCustomerInfoList!!.layoutManager = mLayoutManager
                    rvCustomerInfoList!!.itemAnimator = DefaultItemAnimator()
                    rvCustomerInfoList!!.adapter = customerAdapter
                    customerAdapter!!.notifyDataSetChanged()


                } else {
                    if (Offset == 0) {
                        lblNoRecordsFound!!.visibility = View.VISIBLE
                        rvCustomerInfoList!!.visibility = View.GONE
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
                    Offset = Offset + PageLimit

                    Log.d("OffsetSize", Offset.toString())
                    if (Offset < TotalCountData) {

                        getCustomerDetails()

                    }

                }
            }
        })

    }

    private fun getCustomerDetails() {
        var token = SharedPreference.getSHToken(this)

        dashboardViewmodel!!.getCustomerDetails(
            this,
            token!!,
            PageLimit,
            Offset!!,
            txtFollowUpSearch!!.text.toString(),
            0, customer_type!!, lblFromDate!!.text.toString(),
            lblToDate!!.text.toString()
        )

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

    private fun getTotalCount() {
        var token = SharedPreference.getSHToken(this)
        dashboardViewmodel!!.getCallTotalCount(
            1,
            "",
            CommonUtil.UserData!!.member_id,
            token!!,
            this,
            "Customer"
        )
    }

    private fun customPopupNumbers(number1: String, number2: String) {
        var token = SharedPreference.getSHToken(this)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_popup_numbers)
        val lblNumber1 = dialog.findViewById(R.id.lblNumber1) as TextView
        val lblNumber2 = dialog.findViewById(R.id.lblNumber2) as TextView
        lblNumber1.text = number1
        lblNumber2.text = number2
        val lnrNumber1 = dialog.findViewById(R.id.lnrNumber1) as RelativeLayout
        val lnrNumber2 = dialog.findViewById(R.id.lnrNumber2) as RelativeLayout

        lnrNumber1.setOnClickListener {
            val jsonObject = JsonObject()
            jsonObject.addProperty(ApiRequestNames.Req_CustomerID, customer_id_to_call)
            jsonObject.addProperty(ApiRequestNames.Req_CustomerNumber, number1)
            dashboardViewmodel!!.callCustomer(jsonObject, token!!, this)
            dialog.dismiss()
        }
        lnrNumber2.setOnClickListener {

            val jsonObject = JsonObject()
            jsonObject.addProperty(ApiRequestNames.Req_CustomerID, customer_id_to_call)
            jsonObject.addProperty(ApiRequestNames.Req_CustomerNumber, number2)
            dashboardViewmodel!!.callCustomer(jsonObject, token!!, this)

            dialog.dismiss()
        }
        dialog.show()

    }

    @OnClick(R.id.lnrFromDate)
    fun fromDateClick(view: View) {
        fromDatePicker(view)
    }

    private fun fromDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this@CustomerInfo,
            R.style.MyDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener
            { view, year, monthOfYear, dayOfMonth ->
                lblFromDate!!.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
            },
            year,
            month,
            day
        )

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show()
    }

    @OnClick(R.id.lnrToDate)
    fun toDateClick(view: View) {
        toDatePicker(view)
    }

    private fun toDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this@CustomerInfo,
            R.style.MyDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener
            { view, year, monthOfYear, dayOfMonth ->
                lblToDate!!.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
            },
            year,
            month,
            day
        )

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show()
    }

    @OnClick(R.id.btnSearch)
    fun searchClick() {
        getCustomerDetails()

    }

}
