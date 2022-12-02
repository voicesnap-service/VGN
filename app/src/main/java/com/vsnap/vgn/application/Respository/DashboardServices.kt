package com.vsnap.vgn.application.Respository

import android.app.Activity
import android.app.ProgressDialog
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.*
import com.vsnap.vgn.application.Utils.CustomLoading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardServices {

    var progressDialog: ProgressDialog? = null
    var client_auth: RestClient? = null
    var RecentCallsMutableLiveData: MutableLiveData<RecentCallsResponse?>
    var NotificationMutableLiveData: MutableLiveData<NotificationResponse?>
    var ProfileMutableLiveData: MutableLiveData<ProfileResponse?>
    var CallHistoryMutableLiveData: MutableLiveData<CallHistoryResponse?>
    var CallHistoryCountMutableLiveData: MutableLiveData<GetTotalCountResponse?>
    var CustomerInfoMutableLiveData: MutableLiveData<GetCusomerInfoResponse?>
    var UpdateCustomerInfoMutableLiveData: MutableLiveData<StatusMessageResponse?>
    var blockcustomerMutableLiveData: MutableLiveData<StatusMessageResponse?>
    var callcustomerMutableLiveData: MutableLiveData<StatusMessageResponse?>
    var CallStatusTypeMutableLiveData: MutableLiveData<AvailablityStatusResponse?>
    var GetFollowUpDetailsMutableLiveData: MutableLiveData<GetFollowUpDetailsResponse?>
    var UpdateFollowUpMutableLiveData: MutableLiveData<StatusMessageResponse?>
    var UpdateCallStatusAvailableMutableLiveData: MutableLiveData<StatusMessageResponse?>
    var DeviceTokenUpdationMutableData: MutableLiveData<StatusMessageResponse?>
    var GetCallListCountMutableLiveData: MutableLiveData<GetTotalCountResponse?>
    var GetShareDocumentMutableLiveData: MutableLiveData<GetShareDocumentResponse?>
    var SendWhatsAppDocumentMutableLiveData: MutableLiveData<StatusMessageResponse?>
    var GetNotificationTotalCountMutableData: MutableLiveData<GetTotalCountResponse?>
    var getTemplatesMutableData: MutableLiveData<TemplatesData?>

    init {
        client_auth = RestClient()
        RecentCallsMutableLiveData = MutableLiveData()
        NotificationMutableLiveData = MutableLiveData()
        ProfileMutableLiveData = MutableLiveData()
        CallHistoryMutableLiveData = MutableLiveData()
        CustomerInfoMutableLiveData = MutableLiveData()
        UpdateCustomerInfoMutableLiveData = MutableLiveData()
        blockcustomerMutableLiveData = MutableLiveData()
        callcustomerMutableLiveData = MutableLiveData()
        CallStatusTypeMutableLiveData = MutableLiveData()
        GetFollowUpDetailsMutableLiveData = MutableLiveData()
        UpdateFollowUpMutableLiveData = MutableLiveData()
        UpdateCallStatusAvailableMutableLiveData = MutableLiveData()
        DeviceTokenUpdationMutableData = MutableLiveData()
        GetCallListCountMutableLiveData = MutableLiveData()
        GetShareDocumentMutableLiveData = MutableLiveData()
        SendWhatsAppDocumentMutableLiveData = MutableLiveData()
        GetNotificationTotalCountMutableData = MutableLiveData()
        CallHistoryCountMutableLiveData = MutableLiveData()
        getTemplatesMutableData = MutableLiveData()
    }

    fun GetCallsListByType(
        limit: Int,
        offset: Int,
        keyword: String,
        type: String,
        token: String,
        count: Int, loginId: Int,
        activity: Activity?
    ) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.GetCallListByType(
            limit,
            offset,
            keyword,
            count,
            type,
            loginId,
            token
        )
            ?.enqueue(object : Callback<RecentCallsResponse?> {
                override fun onResponse(
                    call: Call<RecentCallsResponse?>,
                    response: Response<RecentCallsResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "RecentCallsResponse",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            RecentCallsMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        RecentCallsMutableLiveData.postValue(null)

                    } else {
                        RecentCallsMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<RecentCallsResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    RecentCallsMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val RecentCallslistMutableLiveData: LiveData<RecentCallsResponse?>
        get() = RecentCallsMutableLiveData

    fun GetNotification(
        limit: Int,
        offset: Int,
        keyword: Int,
        count: Int,
        login_id: Int,
        token: String,
        activity: Activity?
    ) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        Log.d("testRequesta", "test")
        client_auth!!.apiInterfaces!!.GetNotificationList(
            limit,
            offset,
            keyword.toString(),
            count,
            login_id,
            token
        )
            ?.enqueue(object : Callback<NotificationResponse?> {
                override fun onResponse(
                    call: Call<NotificationResponse?>,
                    response: Response<NotificationResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "NotificationResponse",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            Log.d("testNotiResponse", status.toString())

                            NotificationMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        NotificationMutableLiveData.postValue(null)

                    } else {
                        NotificationMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<NotificationResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    NotificationMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val NotificationListMutableData: LiveData<NotificationResponse?>
        get() = NotificationMutableLiveData




    fun GetTemplates(activity: Activity?, token: String
    ) {
//        progressDialog = CustomLoading.createProgressDialog(activity)
//        progressDialog!!.show()
        Log.d("testRequesta", "test")
        client_auth!!.apiInterfaces!!.GetTemplates(token)
            ?.enqueue(object : Callback<TemplatesData?> {
                override fun onResponse(
                    call: Call<TemplatesData?>,
                    response: Response<TemplatesData?>
                ) {
                    //progressDialog!!.dismiss()
                    Log.d(
                        "NotificationResponse",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            Log.d("testNotiResponse", status.toString())

                            getTemplatesMutableData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                       // progressDialog!!.dismiss()
                        getTemplatesMutableData.postValue(null)

                    } else {
                        getTemplatesMutableData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<TemplatesData?>, t: Throwable) {
                   // progressDialog!!.dismiss()
                    getTemplatesMutableData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val TemplatesMutableData: LiveData<TemplatesData?>
        get() = getTemplatesMutableData


    fun GetNotificationTotalCount(count: Int, login_id: Int, token: String, activity: Activity?) {
        client_auth!!.apiInterfaces!!.GetTotalacountNotificationList(count, login_id, token)
            ?.enqueue(object : Callback<GetTotalCountResponse?> {
                override fun onResponse(
                    call: Call<GetTotalCountResponse?>,
                    response: Response<GetTotalCountResponse?>
                ) {
                    Log.d("CountNotification", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            Log.d("testNotiResponse", status.toString())

                            GetNotificationTotalCountMutableData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        GetNotificationTotalCountMutableData.postValue(null)

                    } else {
                        GetNotificationTotalCountMutableData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<GetTotalCountResponse?>, t: Throwable) {
                    GetNotificationTotalCountMutableData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val GetNotificationTotalCountLiveData: LiveData<GetTotalCountResponse?>
        get() = GetNotificationTotalCountMutableData


    fun GetProfileData(agent_id: Int, token: String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.GetAgentProfile(agent_id, token)
            ?.enqueue(object : Callback<ProfileResponse?> {
                override fun onResponse(
                    call: Call<ProfileResponse?>,
                    response: Response<ProfileResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "ProfileResponse",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            ProfileMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        ProfileMutableLiveData.postValue(null)

                    } else {
                        ProfileMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    ProfileMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val ProfileDataMutableData: LiveData<ProfileResponse?>
        get() = ProfileMutableLiveData


    fun GetCallHistoryList(
        limit: Int,
        offset: Int,
        keyword: String,
        count: Int,
        customer_id: Int,
        agent_id: Int,
        token: String,
        activity: Activity?
    ) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.GetCallHistory(
            limit,
            offset,
            keyword,
            count,
            customer_id,
            agent_id,
            token
        )
            ?.enqueue(object : Callback<CallHistoryResponse?> {
                override fun onResponse(
                    call: Call<CallHistoryResponse?>,
                    response: Response<CallHistoryResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "NotificationResponse",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            CallHistoryMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        CallHistoryMutableLiveData.postValue(null)

                    } else {
                        CallHistoryMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<CallHistoryResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    CallHistoryMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val CallHistoryLiveData: LiveData<CallHistoryResponse?>
        get() = CallHistoryMutableLiveData



    fun GetCallHistoryCount(limit:Int,offset: Int,keyeword:String,customrid:Int,agent_id: Int,count: Int, token: String, activity: Activity?) {
        client_auth!!.apiInterfaces!!.GetCallHistoryCount(limit,offset,keyeword,count,customrid,agent_id, token)
            ?.enqueue(object : Callback<GetTotalCountResponse?> {
                override fun onResponse(
                    call: Call<GetTotalCountResponse?>,
                    response: Response<GetTotalCountResponse?>
                ) {
                    Log.d("CountNotification", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            Log.d("testNotiResponse_his", status.toString())

                            CallHistoryCountMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        CallHistoryCountMutableLiveData.postValue(null)

                    } else {
                        CallHistoryCountMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<GetTotalCountResponse?>, t: Throwable) {
                    CallHistoryCountMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val GetCallHistoryCountLiveData: LiveData<GetTotalCountResponse?>
        get() = CallHistoryCountMutableLiveData

    fun GetCustomerViewInfo(customerId: Int, loginId: Int, token: String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.GetCustomerInfo(customerId, loginId, token)
            ?.enqueue(object : Callback<GetCusomerInfoResponse?> {
                override fun onResponse(
                    call: Call<GetCusomerInfoResponse?>,
                    response: Response<GetCusomerInfoResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "NotificationResponse",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            CustomerInfoMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        CustomerInfoMutableLiveData.postValue(null)

                    } else {
                        CustomerInfoMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<GetCusomerInfoResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    CustomerInfoMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val CustomerViewInfoLiveData: LiveData<GetCusomerInfoResponse?>
        get() = CustomerInfoMutableLiveData


    fun UpdateCustomerInfo(jsonObject: JsonObject, token: String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.UpdateCustomerData(jsonObject, token)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "UpdateCustomerInfo",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            progressDialog!!.dismiss()

                            val status = response.body()!!.status
                            if (status == 1) {
                                UpdateCustomerInfoMutableLiveData.postValue(response.body())

                            }
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        UpdateCustomerInfoMutableLiveData.postValue(null)

                    } else {
                        progressDialog!!.dismiss()
                        UpdateCustomerInfoMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    UpdateCustomerInfoMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val UpdateCustomerDataMutablelivedata: LiveData<StatusMessageResponse?>
        get() = UpdateCustomerInfoMutableLiveData


    fun BlockCustomer(jsonObject: JsonObject,token:String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.BlockCustomer(jsonObject,token)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d("BlockCustomer", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            progressDialog!!.dismiss()

                            val status = response.body()!!.status
                            if (status == 1) {
                                blockcustomerMutableLiveData.postValue(response.body())
                            }
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        blockcustomerMutableLiveData.postValue(null)

                    } else {
                        progressDialog!!.dismiss()
                        blockcustomerMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    blockcustomerMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val BlockCustomerMutableLiveData: LiveData<StatusMessageResponse?>
        get() = blockcustomerMutableLiveData


    fun CallCustomer(jsonObject: JsonObject, token: String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.CallCustomer(jsonObject, token)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d("CallCustomer", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            progressDialog!!.dismiss()

                            val status = response.body()!!.status
                            if (status == 1) {
                                callcustomerMutableLiveData.postValue(response.body())
                            }
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        callcustomerMutableLiveData.postValue(null)

                    } else {
                        progressDialog!!.dismiss()
                        callcustomerMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    callcustomerMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val CallCustomerMutableLiveData: LiveData<StatusMessageResponse?>
        get() = callcustomerMutableLiveData


    fun CallStatusAvailability(token: String, activity: Activity?) {
//        progressDialog = CustomLoading.createProgressDialog(activity)
//        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.CallStatusAvailable(token)
            ?.enqueue(object : Callback<AvailablityStatusResponse?> {
                override fun onResponse(
                    call: Call<AvailablityStatusResponse?>,
                    response: Response<AvailablityStatusResponse?>
                ) {
//                    progressDialog!!.dismiss()
                    Log.d(
                        "CallStatusAvailable",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
//                            progressDialog!!.dismiss()

                            val status = response.body()!!.status
                            if (status == 1) {
                                CallStatusTypeMutableLiveData.postValue(response.body())

                            }
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
//                        progressDialog!!.dismiss()
                        CallStatusTypeMutableLiveData.postValue(null)

                    } else {
//                        progressDialog!!.dismiss()
                        CallStatusTypeMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<AvailablityStatusResponse?>, t: Throwable) {
//                    progressDialog!!.dismiss()
                    CallStatusTypeMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val CallStatusAvailableTypeMutableLiveData: LiveData<AvailablityStatusResponse?>
        get() = CallStatusTypeMutableLiveData


    fun GetFollowUpDetails(
        limit: Int,
        offset: Int,
        keyword: String,
        count: Int,
        fromdate: String,
        todate: String,
        login_id: Int,
        token: String,
        activity: Activity?
    ) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.GetFollowUpReport(
            limit,
            offset,
            keyword,
            count,
            fromdate,
            todate,
            login_id,
            token
        )
            ?.enqueue(object : Callback<GetFollowUpDetailsResponse?> {
                override fun onResponse(
                    call: Call<GetFollowUpDetailsResponse?>,
                    response: Response<GetFollowUpDetailsResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "GetFollowUp",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            progressDialog!!.dismiss()

                            val status = response.body()!!.status
                            if (status == 1) {
                                GetFollowUpDetailsMutableLiveData.postValue(response.body())

                            } else {
                                GetFollowUpDetailsMutableLiveData.postValue(null)

                            }
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        GetFollowUpDetailsMutableLiveData.postValue(null)

                    } else {
                        progressDialog!!.dismiss()
                        GetFollowUpDetailsMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<GetFollowUpDetailsResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    GetFollowUpDetailsMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val GetFollowUpDetailsMutableData: LiveData<GetFollowUpDetailsResponse?>
        get() = GetFollowUpDetailsMutableLiveData


    fun UpdateFollowCompleted(jsonObject: JsonObject, token: String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.UpdateFollowUpcompleted(jsonObject, token)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "UpdateFollowUp",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            progressDialog!!.dismiss()

                            val status = response.body()!!.status
                            if (status == 1) {
                                UpdateFollowUpMutableLiveData.postValue(response.body())
                            }
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        UpdateFollowUpMutableLiveData.postValue(null)

                    } else {
                        progressDialog!!.dismiss()
                        UpdateFollowUpMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    UpdateFollowUpMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val UpdateFollowUpLiveData: LiveData<StatusMessageResponse?>
        get() = UpdateFollowUpMutableLiveData

    fun UpdateCallStatusAvailablity(jsonObject: JsonObject, token: String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.UpdateCallstatusAvailability(jsonObject, token)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d(
                        "UpdatCallStatusAvail",
                        response.code().toString() + " - " + response.toString()
                    )
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            progressDialog!!.dismiss()

                            val status = response.body()!!.status
                            if (status == 1) {
                                UpdateCallStatusAvailableMutableLiveData.postValue(response.body())
                            } else {
                                UpdateCallStatusAvailableMutableLiveData.postValue(null)

                            }
                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        UpdateCallStatusAvailableMutableLiveData.postValue(null)

                    } else {
                        progressDialog!!.dismiss()
                        UpdateCallStatusAvailableMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    UpdateCallStatusAvailableMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val UpdateCallStatusLiveData: LiveData<StatusMessageResponse?>
        get() = UpdateCallStatusAvailableMutableLiveData


    fun DeviceToken(jsonObject: JsonObject, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.DeviceToken(jsonObject)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d("DeviceToken :", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            DeviceTokenUpdationMutableData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        DeviceTokenUpdationMutableData.postValue(null)

                    } else {
                        DeviceTokenUpdationMutableData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    DeviceTokenUpdationMutableData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val DeviceTokenMutableLiveData: LiveData<StatusMessageResponse?>
        get() = DeviceTokenUpdationMutableData

    fun GetCallCount(
        count: Int,
        type: String,
        loginId: Int,
        token: String,
        activity: Activity?
    ) {
        client_auth!!.apiInterfaces!!.GetCallListCount(count, type, loginId, token)
            ?.enqueue(object : Callback<GetTotalCountResponse?> {
                override fun onResponse(
                    call: Call<GetTotalCountResponse?>,
                    response: Response<GetTotalCountResponse?>
                ) {
                    Log.d("GetTotalCallCount :", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            GetCallListCountMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        GetCallListCountMutableLiveData.postValue(null)

                    } else {
                        GetCallListCountMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<GetTotalCountResponse?>, t: Throwable) {
                    GetCallListCountMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val GetCallCountLiveData: LiveData<GetTotalCountResponse?>
        get() = GetCallListCountMutableLiveData

    fun GetShareDocuments(projectId: Int, token: String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.GetShareDocumentList(projectId, token)
            ?.enqueue(object : Callback<GetShareDocumentResponse?> {
                override fun onResponse(
                    call: Call<GetShareDocumentResponse?>,
                    response: Response<GetShareDocumentResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d("DeviceToken :", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            GetShareDocumentMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        GetShareDocumentMutableLiveData.postValue(null)

                    } else {
                        GetShareDocumentMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<GetShareDocumentResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    GetShareDocumentMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val GetShareDocumentLiveData: LiveData<GetShareDocumentResponse?>
        get() = GetShareDocumentMutableLiveData

    fun SubmitDocumentWhatsApp(jsonObject: JsonObject, token: String, activity: Activity?) {
        progressDialog = CustomLoading.createProgressDialog(activity)
        progressDialog!!.show()
        client_auth!!.apiInterfaces!!.SendWhatsAppDocument(jsonObject, token)
            ?.enqueue(object : Callback<StatusMessageResponse?> {
                override fun onResponse(
                    call: Call<StatusMessageResponse?>,
                    response: Response<StatusMessageResponse?>
                ) {
                    progressDialog!!.dismiss()
                    Log.d("DocumentSubmitRes:", response.code().toString() + " - " + response.toString())
                    if (response.code() == 200 || response.code() == 201) {
                        if (response.body() != null) {
                            val status = response.body()!!.status
                            SendWhatsAppDocumentMutableLiveData.postValue(response.body())

                        }
                    } else if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
                        progressDialog!!.dismiss()
                        SendWhatsAppDocumentMutableLiveData.postValue(null)

                    } else {
                        SendWhatsAppDocumentMutableLiveData.postValue(null)

                    }
                }

                override fun onFailure(call: Call<StatusMessageResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    SendWhatsAppDocumentMutableLiveData.postValue(null)
                    t.printStackTrace()
                }
            })
    }

    val SendWhatsAppDocumentLiveData: LiveData<StatusMessageResponse?>
        get() = SendWhatsAppDocumentMutableLiveData
}