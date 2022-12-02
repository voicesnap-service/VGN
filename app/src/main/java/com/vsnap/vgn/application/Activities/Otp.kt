package com.vsnap.vgn.application.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.JsonObject
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.GenericTextWatcher
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Auth


class Otp : AppCompatActivity() {
    var OTPValue: String? = null

    @JvmField
    @BindView(R.id.txtOne)
    var txtOne: EditText? = null

    @JvmField
    @BindView(R.id.txtTwo)
    var txtTwo: EditText? = null

    @JvmField
    @BindView(R.id.txtThree)
    var txtThree: EditText? = null

    @JvmField
    @BindView(R.id.txtFour)
    var txtFour: EditText? = null

    @JvmField
    @BindView(R.id.txtFive)
    var txtFive: EditText? = null

    @JvmField
    @BindView(R.id.txtSix)
    var txtSix: EditText? = null
    var authViewModel: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_screen)

        ButterKnife.bind(this)

        authViewModel = ViewModelProvider(this).get(Auth::class.java)
        authViewModel!!.init()


        authViewModel!!.OtpMutableLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    val i = Intent(this, Createpassword::class.java)
                    i.putExtra("ScreenType", true)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)

                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.ApiAlert(this, "Something went wrong")

            }
        }

        val edit = arrayOf<EditText>(txtOne!!, txtTwo!!, txtThree!!, txtFour!!, txtFive!!, txtSix!!)
        txtOne!!.addTextChangedListener(GenericTextWatcher(txtOne!!, edit))
        txtTwo!!.addTextChangedListener(GenericTextWatcher(txtTwo!!, edit))
        txtThree!!.addTextChangedListener(GenericTextWatcher(txtThree!!, edit))
        txtFour!!.addTextChangedListener(GenericTextWatcher(txtFour!!, edit))
        txtFive!!.addTextChangedListener(GenericTextWatcher(txtFive!!, edit))
        txtSix!!.addTextChangedListener(GenericTextWatcher(txtSix!!, edit))

    }


    @OnClick(R.id.imgotpback)
    fun clickback() {

        onBackPressed()
    }


    @OnClick(R.id.btnVerify)
    fun OtpVerify() {

        val otp = txtOne!!.text.toString() + txtTwo!!.text.toString() + txtThree!!.text.toString() +
                txtFour!!.text.toString() + txtFive!!.text.toString() + txtSix!!.text.toString()

        var emailid = SharedPreference.getShH_Email(this)

        if (!otp.equals("")) {
            val jsonObject = JsonObject()
//            Log.d("Email", Email!!)
            jsonObject.addProperty(ApiRequestNames.Req_EmailID, emailid)
            jsonObject.addProperty(ApiRequestNames.Req_OtpValidate, otp)
            authViewModel!!.veifyOtp(jsonObject, this)

        } else {
            CommonUtil.CommonAlertOk(this, "Enter a valid otp")
        }


    }


}