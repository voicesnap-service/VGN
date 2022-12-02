package com.vsnap.vgn.application.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.VerifyEmailData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Auth

class EnterEmail : AppCompatActivity() {
    var Email: String? = null

    @JvmField
    @BindView(R.id.edMail)
    var edMail: EditText? = null
    var authViewModel: Auth? = null
    var EmailData: List<VerifyEmailData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_screen)
        ButterKnife.bind(this)
        authViewModel = ViewModelProvider(this).get(Auth::class.java)
        authViewModel!!.init()

        authViewModel!!.verifyEmailLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    EmailData = response.data!!
                    CommonUtil.VerifyEmailData = response.data!!
                    if (EmailData.size != 0) {
                        SharedPreference.putEmailID(this, Email!!)
                        if (EmailData.get(0).is_redirect_to_otp.equals("1")) {
                            OTPScreenIntent()
                        } else {
                            EnterPasswordScreen()
                        }
                    } else {
                        CommonUtil.ApiAlert(this, message)
                    }
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                CommonUtil.ApiAlert(this, "Something went wrong")

            }
        }


    }

    private fun EnterPasswordScreen() {
        val i = Intent(this, Createpassword::class.java)
        i.putExtra("ScreenType", true)
        i.putExtra("Email", Email)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    private fun OTPScreenIntent() {
        val i = Intent(this, Otp::class.java)
        i.putExtra("ScreenType", true)
        i.putExtra("Email", Email)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    fun ClickNext(view: View) {
        Email = edMail!!.text.toString()
        if (isEmailValid(Email)) {
            val jsonObject = JsonObject()
            Log.d("Email", Email!!)
            jsonObject.addProperty(ApiRequestNames.Req_EmailID, Email)
            authViewModel!!.verifyemail(jsonObject, this)

        } else {
            CommonUtil.CommonAlertOk(this, "Please enter a valid email")
        }
    }


    @OnClick(R.id.lblTermsAndCondition)
    fun termsNconditionClick() {
        CommonUtil.TermsAndConditions(this, true)

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}