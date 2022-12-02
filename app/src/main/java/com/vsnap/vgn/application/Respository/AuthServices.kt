package com.vsnap.vgn.application.Respository

import android.app.Activity
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.*
import com.vsnap.vgn.application.Utils.CustomLoading
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AuthServices {
    var progressDialog: ProgressDialog? = null
    var client_auth: RestClient? = null
    var verifyEmailMutableData: MutableLiveData<VerifyEmailResponse?>
    var verifyOtpMutableData: MutableLiveData<StatusMessageResponse?>
    var updatePasswordMutableData: MutableLiveData<StatusMessageResponse?>
    var UserlogindetailMutableData: MutableLiveData<LoginResponse?>
    var ChangePasswordMutableData: MutableLiveData<StatusMessageResponse?>
    var VersionCheckMutableLiveData: MutableLiveData<VersionCheckResponse?>


    init {
        client_auth = RestClient()
        verifyEmailMutableData = MutableLiveData()
        verifyOtpMutableData = MutableLiveData()
        updatePasswordMutableData = MutableLiveData()
        UserlogindetailMutableData = MutableLiveData()
        VersionCheckMutableLiveData = MutableLiveData()
        VersionCheckMutableLiveData = MutableLiveData()
        ChangePasswordMutableData = MutableLiveData()
    }

    fun VersionCheckApi(DeviceType:String,versioncode:Int, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.VersionCheck(DeviceType,versioncode)
            ?.enqueue(object : Callback<VersionCheckResponse?> {
                override fun onResponse(
                    call: Call<VersionCheckResponse?>,
                    response: Response<VersionCheckResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "VersionCheckRes",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            VersionCheckMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        VersionCheckMutableLiveData.postValue(null)

                    } else {
                        VersionCheckMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<VersionCheckResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    VersionCheckMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val versioncheckmutableLiveData: LiveData<VersionCheckResponse?>
        get() = VersionCheckMutableLiveData

    fun VerifyEmail(jsonObject: JsonObject, activity: Activity?) {

        Log.d("testlog", jsonObject.toString())
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.VerifyEmailApi(jsonObject)
            ?.enqueue(object : Callback<VerifyEmailResponse?> {
                override fun onResponse(
                    call: Call<VerifyEmailResponse?>,
                    response: Response<VerifyEmailResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d("EmailVerification", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            verifyEmailMutableData.postValue(response.body())
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        verifyEmailMutableData.postValue(response.body())

                    } else {
                        verifyEmailMutableData.postValue(response.body())

                    }
                }
                override fun onFailure(call: Call<VerifyEmailResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    verifyEmailMutableData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val VerifyEmailMutableLiveData: LiveData<VerifyEmailResponse?>
        get() = verifyEmailMutableData


    fun VerifyOTP(jsonObject: JsonObject, activity: Activity?) {

        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.VerifyOTP(jsonObject)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d("OTPVerification", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            verifyOtpMutableData.postValue(response.body())
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        verifyOtpMutableData.postValue(response.body())

                    } else {
                        verifyOtpMutableData.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    verifyOtpMutableData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val VerifyOtpMutableLiveData: LiveData<StatusMessageResponse?>
        get() = verifyOtpMutableData


    fun UpdatePassword(jsonObject: JsonObject, activity: Activity?) {

        progressDialog = CustomLoading.createProgressDialog(activity)
//        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.PasswordUpdation(jsonObject)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
//                    progressDialog!!.dismiss()
                    Log.d("updatePassword", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {

                            Log.d("testResponsePassord",response.body()!!.status.toString())
                            val status = response.body()!!.status
                            if(status == 1){
                                updatePasswordMutableData.postValue(response.body())

                            }
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        updatePasswordMutableData.postValue(null)

                    } else {
                        updatePasswordMutableData.postValue(null)

                    }

                }
                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    updatePasswordMutableData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val UpdatePasswordMutablelivedata: LiveData<StatusMessageResponse?>
        get() = updatePasswordMutableData


    fun GetUserDetails(jsonObject: JsonObject, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.UserLoginDetails(jsonObject)
            ?.enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "LoginDetails",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            UserlogindetailMutableData.postValue(response.body())


                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        UserlogindetailMutableData.postValue(null)

                    } else {
                        UserlogindetailMutableData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    updatePasswordMutableData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val userLoginDetailMutableLiveData: LiveData<LoginResponse?>
        get() = UserlogindetailMutableData


    fun ChangePasswordApi(jsonObject: JsonObject, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.ChangePasswordApi(jsonObject)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "ChangePasswords",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            ChangePasswordMutableData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        ChangePasswordMutableData.postValue(null)

                    } else {
                        ChangePasswordMutableData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    ChangePasswordMutableData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val changepasswordMutableLiveData: LiveData<StatusMessageResponse?>
        get() = ChangePasswordMutableData





}