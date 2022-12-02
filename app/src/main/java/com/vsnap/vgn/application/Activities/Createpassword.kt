package com.vsnap.vgn.application.Activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.CallHistoryData
import com.vsnap.vgn.application.Modal.LoginData
import com.vsnap.vgn.application.Modal.LoginResponse
import com.vsnap.vgn.application.Modal.LoginUserDetails
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Auth
import org.mindrot.jbcrypt.BCrypt
import java.util.ArrayList
import java.util.regex.Pattern


class Createpassword : AppCompatActivity() {
    @JvmField
    @BindView(R.id.lblPasswordTitle)
    var lblPasswordTitle: TextView? = null

    @JvmField
    @BindView(R.id.lblPassword)
    var lblPassword: TextView? = null

    @JvmField
    @BindView(R.id.lblConfirmPassword)
    var lblConfirmPassword: TextView? = null

    @JvmField
    @BindView(R.id.edNewpassword)
    var edNewpassword: EditText? = null

    @JvmField
    @BindView(R.id.edConfirmpassword)
    var edConfirmpassword: EditText? = null

    @JvmField
    @BindView(R.id.imgNewPassword)
    var imgNewPassword: ImageView? = null

    @JvmField
    @BindView(R.id.imgConfirmPassword)
    var imgConfirmPassword: ImageView? = null

    @JvmField
    @BindView(R.id.layoutConfirmPassword)
    var layoutConfirmPassword: ConstraintLayout? = null

    @JvmField
    @BindView(R.id.layoutNewpassword)
    var layoutNewpassword: ConstraintLayout? = null

    @JvmField
    @BindView(R.id.btnConfirmpassword)
    var btnConfirmpassword: Button? = null

    @JvmField
    @BindView(R.id.lblUserEmail)
    var lblUserEmail: TextView? = null

    var type: Boolean? = null
    var NewPassword: String? = null
    var ConfirmPassword: String? = null
    var EmailID: String? = null
    var passwordvisible = true
    var authViewModel: Auth? = null
    var GetLoginData: List<LoginData> = ArrayList()
    var GetUserDetailsData: LoginUserDetails ? = null
    var UserLogin = false

    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[@#$%^&+=])" +  // at least 1 special character
                "(?=\\S+$)" +  // no white spaces
                ".{8,}" +  // at least 4 characters
                "$"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_password)
        ButterKnife.bind(this)
        type = intent.getBooleanExtra("ScreenType", false)

        EmailID = SharedPreference.getShH_Email(this)

        lblUserEmail!!.text = EmailID
        if (type!!) {
            lblPasswordTitle!!.setText(R.string.txt_create_password)
        } else {
            lblPasswordTitle!!.setText(R.string.txt_forgot_password)
            lblPassword!!.setText(R.string.txt_new_password)
            lblConfirmPassword!!.setText(R.string.txt_confirm_new_password)
        }

        if(CommonUtil.VerifyEmailData.get(0).is_redirect_to_otp.equals("0")){
            lblPasswordTitle!!.setText(R.string.txt_enter_password)
            layoutNewpassword!!.visibility=View.GONE
            btnConfirmpassword!!.setText("Submit")
        }else{
            lblPasswordTitle!!.setText(R.string.txt_create_password)
            layoutNewpassword!!.visibility=View.VISIBLE
        }
        authViewModel = ViewModelProvider(this).get(Auth::class.java)
        authViewModel!!.init()
        authViewModel!!.PasswordMutableLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message

                Log.d("login_Status", status.toString())

                if (status == 1) {
                    Log.d("PasswordSucces", message!!)

                    SharedPreference.putPassword(this, ConfirmPassword!!)

                    EmailAndPasswordRequest(false)

                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                Log.d("PasswordError", "test")
                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }

        authViewModel!!.LogindetailsLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                Log.d("login_Status", status.toString())

                if (status == 1) {
                    GetLoginData = response.data!!
                    SharedPreference.putToken(this, GetLoginData.get(0).token)
                    GetUserDetailsData = GetLoginData.get(0).UserDetails!!
                    CommonUtil.UserData = GetLoginData.get(0).UserDetails
                    val i = Intent(this, DashboardHome::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finishAffinity()
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                Log.d("loginError", "test")

                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }

    }


    fun BacktoEmail(view: View) {
        onBackPressed()

    }

    @OnClick(R.id.btnConfirmpassword)
    fun ConfirmPassword() {

        NewPassword = edNewpassword!!.text.toString()
        ConfirmPassword = edConfirmpassword!!.text.toString()

//        if (NewPassword!!.isNullOrEmpty()) {
//            CommonUtil.CommonAlertOk(this, getString(R.string.txt_empty_password))
//        }
         if (ConfirmPassword!!.isNullOrEmpty()) {
            CommonUtil.CommonAlertOk(this, getString(R.string.txt_empty_password))
        }

//         else if (NewPassword != ConfirmPassword) {
//            CommonUtil.CommonAlertOk(this, getString(R.string.txt_password_notsimilar))
//        }
//
//         else if (!validatePassword(NewPassword!!) && !validatePassword(ConfirmPassword!!)) {
//            CommonUtil.passwordAlert(this)
//        }

         else {
             ConfirmPassword = edConfirmpassword!!.text.toString()
            Log.d("EncryptedPassword", ConfirmPassword!!)
            EmailAndPasswordRequest(true)

        }
    }

    private fun EmailAndPasswordRequest(value: Boolean) {

        if (value) {
            val jsonObject = JsonObject()
            jsonObject.addProperty(ApiRequestNames.Req_EmailID, EmailID)
            jsonObject.addProperty(ApiRequestNames.Req_password, ConfirmPassword)
            authViewModel!!.UpdatePassword(jsonObject, this)
        } else {

            var storedpassword = SharedPreference.getSH_Password(this)
            var EncryptedPassword = BCrypt.hashpw(storedpassword, BCrypt.gensalt())
            SharedPreference.putEncryptedPassword(this, EncryptedPassword!!)

            val jsonObject = JsonObject()
            jsonObject.addProperty(ApiRequestNames.Req_EmailID, EmailID)
            jsonObject.addProperty(ApiRequestNames.Req_password, EncryptedPassword)
            authViewModel!!.getUserDetails(jsonObject, this)
        }

    }

    @OnClick(R.id.imgNewPassword)
    fun newpasswordVisible() {
        passwordHideandShow(edNewpassword, imgNewPassword)
    }

    @OnClick(R.id.imgConfirmPassword)
    fun ConfirmVisible() {
        passwordHideandShow(edConfirmpassword, imgConfirmPassword)
    }

    private fun passwordHideandShow(txtPassword: EditText?, imgEye: ImageView?) {
        if (passwordvisible == true) {
            txtPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            txtPassword?.setSelection(txtPassword?.text!!.length)
            passwordvisible = false
            imgEye?.setImageResource(R.drawable.ic_eye)
        } else {
            txtPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
            txtPassword?.setSelection(txtPassword?.text!!.length)
            passwordvisible = true
            imgEye?.setImageResource(R.drawable.password_visible)
        }
    }

    private fun validatePassword(passwordInput: String): Boolean {
        return if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            false
        } else {
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}