package com.vsnap.vgn.application.Activities

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Adapters.CallStatusAvailableType
import com.vsnap.vgn.application.Interface.CallStatusListener
import com.vsnap.vgn.application.Modal.AvailabilityStatusData
import com.vsnap.vgn.application.Modal.ProfileData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.CommonUtil.CallAvailableType
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Dashboard
import okhttp3.internal.notify

class Profile : AppCompatActivity() {

    var dashboardViewmodel: Dashboard? = null
    var GetprofileData: List<ProfileData> = ArrayList()
    var GetAvailablestatusData: List<AvailabilityStatusData> = ArrayList()
    var adapter: CallStatusAvailableType? = null
    var popupBlockWindow: PopupWindow? = null

    var row_index = 0
    @JvmField
    @BindView(R.id.lblprofileAgentName)
    var lblprofileAgentName: TextView? = null

    @JvmField
    @BindView(R.id.LayoutSipNumber)
    var LayoutSipNumber: RelativeLayout? = null

    @JvmField
    @BindView(R.id.lblRoleType)
    var lblRoleType: TextView? = null

    @JvmField
    @BindView(R.id.lblMail)
    var lblMail: TextView? = null

    @JvmField
    @BindView(R.id.lblMobilenumber)
    var lblMobilenumber: TextView? = null

    @JvmField
    @BindView(R.id.lblSIPNumber)
    var lblSIPNumber: TextView? = null

    @JvmField
    @BindView(R.id.lblAvailableType)
    var lblAvailableType: TextView? = null

    @JvmField
    @BindView(R.id.lblReportingTo)
    var lblReportingTo: TextView? = null

    @JvmField
    @BindView(R.id.recycelrCallStatus)
    var recycelrCallStatus: RecyclerView? = null


    var AvailableType :String? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.actionbar_layout)
        val view = supportActionBar!!.customView
        val lblTitle = view.findViewById<View>(R.id.lblTitle) as TextView
        val imgBack = view.findViewById<View>(R.id.imgBack) as ImageView
        val rytAction = view.findViewById<View>(R.id.rytAction) as RelativeLayout
        ButterKnife.bind(this)

        lblTitle.text = "Profile"
        imgBack.setOnClickListener({
            onBackPressed()
        })

        dashboardViewmodel = ViewModelProvider(this).get(Dashboard::class.java)
        dashboardViewmodel!!.init()


        dashboardViewmodel!!.ProfileLiveData!!.observe(this) { response ->
            if (response != null) {
                var token = SharedPreference.getSHToken(this)
                dashboardViewmodel!!.callStatusAvailability(token!!, this)
                Log.d("ProfileData", "test")
                val status = response.status
                val message = response.message
                if (status == 1) {
                    GetprofileData = response.data!!
                    SetProfileData()
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.ApiAlert(this, "Try again something went wrong")
            }
        }

        dashboardViewmodel!!.CallStatusAvailabilityType!!.observe(this) { response ->
            if (response != null) {
                Log.d("Availablestatus", "test")
                val status = response.status
                val message = response.message
                if (status == 1) {
                    GetAvailablestatusData = response.data!!
                    adapter =
                        CallStatusAvailableType(
                            GetAvailablestatusData,
                            this,
                            object : CallStatusListener {
                                override fun onStatusClick(
                                    holder: CallStatusAvailableType.MyViewHolder,
                                    data: AvailabilityStatusData
                                ) {
                                    holder.layoutAvailablestatus.setOnClickListener(
                                        {


                                            if (data.unnest.equals("Available")) {
                                                holder.layoutAvailablestatus.setBackgroundResource(R.drawable.bg_available_selected_green)
                                                holder.lblAvailabilityType.setTextColor(
                                                    Color.parseColor(
                                                        "#FFFFFF"
                                                    )
                                                )
                                                PopUpForBlocking(view,data.unnest!!)
                                            } else {
                                                holder.layoutAvailablestatus.setBackgroundResource(R.drawable.bg_available_selected_red)
                                                holder.lblAvailabilityType.setTextColor(
                                                    Color.parseColor(
                                                        "#FFFFFF"
                                                    )
                                                )

                                                PopUpForBlocking(view,data.unnest!!)

                                            }
                                        }

                                    )                                }
                            })
                    val mLayoutManager: RecyclerView.LayoutManager =
                        GridLayoutManager(applicationContext, 3)
                    recycelrCallStatus!!.layoutManager = mLayoutManager
                    recycelrCallStatus!!.isNestedScrollingEnabled = false
                    recycelrCallStatus!!.addItemDecoration(GridSpacingItemDecoration(3, false))
                    recycelrCallStatus!!.itemAnimator = DefaultItemAnimator()
                    recycelrCallStatus!!.adapter = adapter

                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.ApiAlert(this, "Try again something went wrong")
            }
        }

        dashboardViewmodel!!.UpdateCallStatusAvailMutableLiveData!!.observe(this) { response ->
            if (response != null) {
                Log.d("UpdateCallStatus", "UpdateCallStatus")
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

    private fun SetProfileData() {
        lblprofileAgentName!!.setText(GetprofileData.get(0).member_name)
        lblRoleType!!.setText(GetprofileData.get(0).user_type)
        lblMail!!.setText(GetprofileData.get(0).email_id)
        lblMobilenumber!!.setText(GetprofileData.get(0).mobile_number)

        if(!GetprofileData.get(0).soft_phone_ip.equals("")){
            lblSIPNumber!!.setText(GetprofileData.get(0).soft_phone_ip)
        }else{
            LayoutSipNumber!!.visibility=View.GONE
        }
        lblAvailableType!!.setText(GetprofileData.get(0).call_availability)
        lblReportingTo!!.setText(GetprofileData.get(0).reporting_to)

        CallAvailableType = GetprofileData.get(0).call_availability
    }

    class GridSpacingItemDecoration(private val spanCount: Int, includeEdge: Boolean) :
        RecyclerView.ItemDecoration() {
        private var spacing = 3
        private val includeEdge: Boolean
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column - 1) * spacing / spanCount
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }

        init {
            spacing = spacing
            this.includeEdge = includeEdge
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun UpdateCallStatusAvailable(callAvailable: String) {
        var token = SharedPreference.getSHToken(this)
        val jsonObject = JsonObject()
        jsonObject.addProperty(ApiRequestNames.Req_MemberID, CommonUtil.UserData!!.member_id)
        jsonObject.addProperty(ApiRequestNames.Req_CallAvailability, callAvailable)
        jsonObject.addProperty(ApiRequestNames.Req_Remarks, "")
        dashboardViewmodel!!.updateCallStatusAvailability(jsonObject, token!!, this)
    }

    private fun PopUpForBlocking(v: View,availabletype:String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = inflater.inflate(R.layout.popup_blocking, null)
        popupBlockWindow = PopupWindow(
            layout,
            android.app.ActionBar.LayoutParams.MATCH_PARENT,
            android.app.ActionBar.LayoutParams.WRAP_CONTENT,
            true
        )
        popupBlockWindow!!.contentView = layout
        val edReason = layout.findViewById<View>(R.id.edReason) as EditText
        val btnBlock = layout.findViewById<View>(R.id.btnBlock) as Button
        val imgclose = layout.findViewById<View>(R.id.imgclose) as ImageView
        val lblReasonBlcok = layout.findViewById<View>(R.id.lblReasonBlcok) as TextView

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

        lblReasonBlcok.text= "Reason For Availability"
        btnBlock.text=availabletype
        btnBlock.setOnClickListener {

            UpdateCallStatusAvailable(availabletype)

        }
        imgclose.setOnClickListener({

            adapter!!.notifyDataSetChanged()
            popupBlockWindow!!.dismiss()
        })

    }

    override fun onResume() {
        var token = SharedPreference.getSHToken(this)
        dashboardViewmodel!!.getProfileData(CommonUtil.UserData!!.member_id, token!!, this)
        super.onResume()

    }

}