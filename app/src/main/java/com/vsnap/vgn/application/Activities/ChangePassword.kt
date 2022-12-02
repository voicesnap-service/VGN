package com.vsnap.vgn.application.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.JsonObject
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Auth
import java.util.regex.Pattern

class ChangePassword : AppCompatActivity() {
    @JvmField
    @BindView(R.id.lblPasswordTitle)
    var lblPasswordTitle: TextView? = null

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
    @BindView(R.id.edOldPassword)
    var edOldPassword: EditText? = null
    @JvmField
    @BindView(R.id.imgNewPassword)
    var imgNewPassword: ImageView? = null

    @JvmField
    @BindView(R.id.imgConfirmPassword)
    var imgConfirmPassword: ImageView? = null

    @JvmField
    @BindView(R.id.imgOldPassword)
    var imgOldPassword: ImageView? = null


    var type: Boolean? = null
    var NewPassword: String? = null
    var ConfirmPassword: String? = null
    var OldPassword: String? = null
    var EncryptedPassword: String? = null
    var EmailID: String? = null
    var passwordvisible = true
    var authViewModel: Auth? = null

    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[@#$%^&+=])" +  // at least 1 special character
                "(?=\\S+$)" +  // no white spaces
                ".{8,}" +  // at least 4 characters
                "$"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        ButterKnife.bind(this)


        authViewModel = ViewModelProvider(this).get(Auth::class.java)
        authViewModel!!.init()

        authViewModel!!.ChangePasswordLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    Log.d("PasswordSucces", message!!)
                    SharedPreference.clearShLogin(this)

                    ApiAlert(this, message,true)

                } else {
                    ApiAlert(this, message,false)
                }
            } else {
                Log.d("PasswordError", "test")
                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }
    }
    fun ApiAlert(activity: Activity?, msg: String?,value: Boolean) {
        if (activity != null) {
            val dlg = androidx.appcompat.app.AlertDialog.Builder(activity)
            dlg.setTitle("Info")
            dlg.setMessage(msg)
            dlg.setPositiveButton("OK") { dialog, which ->
                if(value){

                    val i = Intent(this, EnterEmail::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(i)

                }else{
                    dlg.setCancelable(true)
                }


            }
            dlg.setCancelable(false)
            dlg.create()
            dlg.show()
        }
    }
    @OnClick(R.id.btnConfirmpassword)
    fun buttonClick() {

        NewPassword = edNewpassword!!.text.toString()
        ConfirmPassword = edConfirmpassword!!.text.toString()
        OldPassword = edOldPassword!!.text.toString()
        EmailID = SharedPreference.getShH_Email(this)

        if (NewPassword!!.isNullOrEmpty()) {
            CommonUtil.CommonAlertOk(this, getString(R.string.txt_empty_password))
        } else if (ConfirmPassword!!.isNullOrEmpty()) {
            CommonUtil.CommonAlertOk(this, getString(R.string.txt_empty_password))
        } else if (NewPassword != ConfirmPassword) {
            CommonUtil.CommonAlertOk(this, getString(R.string.txt_password_notsimilar))
        }
//        else if (!validatePassword(NewPassword!!) && !validatePassword(ConfirmPassword!!)) {
//            CommonUtil.passwordAlert(this)
//        }
        else {
            val jsonObject = JsonObject()
            jsonObject.addProperty(ApiRequestNames.Req_EmailID, EmailID)
            jsonObject.addProperty(ApiRequestNames.Req_Old_password, OldPassword)
            jsonObject.addProperty(ApiRequestNames.Req_New_password, NewPassword)
            authViewModel!!.changepassword(jsonObject, this)

        }

    }

    private fun validatePassword(passwordInput: String): Boolean {
        return if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            false
        } else {
            true
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

    @OnClick(R.id.imgOldPassword)
    fun OldPasswordvisible() {
        passwordHideandShow(edOldPassword, imgOldPassword)
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

    @OnClick(R.id.imgpasswordback)
    fun onbackClick(){
        onBackPressed()
    }
}