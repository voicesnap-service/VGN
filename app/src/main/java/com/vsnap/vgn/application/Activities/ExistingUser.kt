package com.vsnap.vgn.application.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.LoginData
import com.vsnap.vgn.application.Modal.LoginUserDetails
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Auth
import org.mindrot.jbcrypt.BCrypt
import java.util.ArrayList

class ExistingUser : AppCompatActivity() {
    var EmailID: String? = null
    var Password: String? = null

    @JvmField
    @BindView(R.id.edUserEmail)
    var edUserEmail: TextView? = null

    @JvmField
    @BindView(R.id.edPassword)
    var edPassword: TextView? = null

    @JvmField
    @BindView(R.id.lblReportingTo)
    var lblReportingTo: TextView? = null

    var authViewModel: Auth? = null
    var GetLoginData: List<LoginData> = ArrayList()
    var GetUserDetailsData: LoginUserDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_user)
        ButterKnife.bind(this)

        CommonUtil.LogoutType = 1


        authViewModel = ViewModelProvider(this).get(Auth::class.java)
        authViewModel!!.init()

        authViewModel!!.LogindetailsLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                GetLoginData = response.data!!
                if (status == 1) {
                    SharedPreference.putToken(this, GetLoginData.get(0).token)
                    GetUserDetailsData = GetLoginData.get(0).UserDetails!!
                    CommonUtil.UserData = GetLoginData.get(0).UserDetails!!
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

    @OnClick(R.id.lblForgotPasswword)
    fun forgotpassword() {
        val i = Intent(this, Createpassword::class.java)
        i.putExtra("ScreenType", false)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    @OnClick(R.id.btnLogin)
    fun UserLogin() {

        Log.d("Click", "test")
        EmailID = edUserEmail!!.text.toString()
        Password = edPassword!!.text.toString()
        SharedPreference.putPassword(this, Password!!)
        SharedPreference.putEmailID(this, EmailID!!)

        var EncryptedPassword = BCrypt.hashpw(Password, BCrypt.gensalt())

        if (!EmailID.equals("") && !Password.equals("")) {
            Log.d("UserDetailsApi", EmailID.toString())
            val jsonObject = JsonObject()
            jsonObject.addProperty(ApiRequestNames.Req_EmailID, EmailID)
            jsonObject.addProperty(ApiRequestNames.Req_password, EncryptedPassword)
            authViewModel!!.getUserDetails(jsonObject, this)
        } else {
            CommonUtil.ApiAlert(this, "Please Fill Credentials")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}