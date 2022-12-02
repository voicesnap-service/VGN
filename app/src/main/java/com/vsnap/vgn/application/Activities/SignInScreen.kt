//package com.vsnap.vgn.application.Activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import butterknife.BindView
//import butterknife.OnClick
//import com.vsnap.vgn.application.R
//import com.vsnap.vgn.application.Utils.SharedPreference
//
//class SignInScreen  : AppCompatActivity() {
//    var EmailID: String? = null
//    var Password: String? = null
//
//    @JvmField
//    @BindView(R.id.edUserEmail)
//    var edUserEmail: TextView? = null
//
//    @JvmField
//    @BindView(R.id.edPassword)
//    var edPassword: TextView? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_existing_user)
//
//    }
//
//    @OnClick(R.id.lblForgotPasswword)
//    fun forgotpassword() {
//
//        val i = Intent(this, Createpassword::class.java)
//        i.putExtra("ScreenType", false)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(i)
//
//    }
//
//    @OnClick(R.id.btnSignIn)
//    fun UserLogin() {
//        EmailID = edUserEmail!!.text.toString()
//        Password = edPassword!!.text.toString()
//        SharedPreference.putPassword(this, Password!!)
//    }
//}