package com.vsnap.vgn.application.ViewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.LoginResponse
import com.vsnap.vgn.application.Modal.StatusMessageResponse
import com.vsnap.vgn.application.Modal.VerifyEmailResponse
import com.vsnap.vgn.application.Modal.VersionCheckResponse
import com.vsnap.vgn.application.Respository.AuthServices

class Auth (application: Application) : AndroidViewModel(application){
    private var apiRepositories: AuthServices? = null

    var verifyEmailLiveData: LiveData<VerifyEmailResponse?>? = null
        private set

    var OtpMutableLiveData: LiveData<StatusMessageResponse?>? = null
        private set

    var PasswordMutableLiveData: LiveData<StatusMessageResponse?>? = null
        private set

    var LogindetailsLiveData: LiveData<LoginResponse?>? = null
    var VersionCheckLiveData: LiveData<VersionCheckResponse?>? = null
    var ChangePasswordLiveData: LiveData<StatusMessageResponse?>? = null

    fun init() {
        apiRepositories = AuthServices()
        verifyEmailLiveData = apiRepositories!!.VerifyEmailMutableLiveData
        OtpMutableLiveData = apiRepositories!!.VerifyOtpMutableLiveData
        PasswordMutableLiveData = apiRepositories!!.UpdatePasswordMutablelivedata
        LogindetailsLiveData = apiRepositories!!.userLoginDetailMutableLiveData
        VersionCheckLiveData = apiRepositories!!.versioncheckmutableLiveData
        ChangePasswordLiveData = apiRepositories!!.changepasswordMutableLiveData
    }

    fun VersionCheck(deviceType:String,versioncode:Int, activity: Activity?) {
        apiRepositories!!.VersionCheckApi(deviceType,versioncode, activity)
    }

    fun verifyemail(jsonObject: JsonObject, activity: Activity?) {
        apiRepositories!!.VerifyEmail(jsonObject, activity)
    }

    fun veifyOtp(jsonObject: JsonObject, activity: Activity?) {
        apiRepositories!!.VerifyOTP(jsonObject, activity)
    }

    fun UpdatePassword(jsonObject: JsonObject, activity: Activity?) {
        apiRepositories!!.UpdatePassword(jsonObject, activity)
    }

    fun getUserDetails(jsonObject: JsonObject, activity: Activity?) {
        apiRepositories!!.GetUserDetails(jsonObject, activity)
    }
    fun changepassword(jsonObject: JsonObject, activity: Activity?) {
        apiRepositories!!.ChangePasswordApi(jsonObject, activity)
    }
}