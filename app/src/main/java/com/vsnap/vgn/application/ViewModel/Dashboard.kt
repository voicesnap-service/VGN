package com.vsnap.vgn.application.ViewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.*
import com.vsnap.vgn.application.Respository.DashboardServices

class Dashboard(application: Application) : AndroidViewModel(application) {
    private var apiRepositories: DashboardServices? = null
    var RecentCallsLiveData: LiveData<RecentCallsResponse?>? = null
    var NotificationLiveData: LiveData<NotificationResponse?>? = null
    var ProfileLiveData: LiveData<ProfileResponse?>? = null
    var CallHistoryLiveData: LiveData<CallHistoryResponse?>? = null
    var GetCallHistoryCountLiveData: LiveData<GetTotalCountResponse?>? = null
    var CustomerInfoLiveData: LiveData<GetCusomerInfoResponse?>? = null
    var UpdateCustomerInfoLiveData: LiveData<StatusMessageResponse?>? = null
    var BlockCustomerCustomerLiveData: LiveData<StatusMessageResponse?>? = null
    var CallCustomerLiveData: LiveData<StatusMessageResponse?>? = null
    var CallStatusAvailabilityType: LiveData<AvailablityStatusResponse?>? = null
    var GetUpdateFollowUpMutableLiveData: LiveData<GetFollowUpDetailsResponse?>? = null
    var GetFollowUpStatusMutableLiveData: LiveData<StatusMessageResponse?>? = null
    var UpdateCallStatusAvailMutableLiveData: LiveData<StatusMessageResponse?>? = null
    var DeviceTokenMutableLiveData: LiveData<StatusMessageResponse?>? = null
    var GetCallListCountLiveData: LiveData<GetTotalCountResponse?>? = null
    var GetShareDocumentMutableLiveData: LiveData<GetShareDocumentResponse?>? = null
    var SubmitShareDocumentLiveData: LiveData<StatusMessageResponse?>? = null
    var NotificationTotalCountLiveData: LiveData<GetTotalCountResponse?>? = null

    var TemplatesLiveData: LiveData<TemplatesData?>? = null


    fun init() {
        apiRepositories = DashboardServices()
        RecentCallsLiveData = apiRepositories!!.RecentCallslistMutableLiveData
        NotificationLiveData = apiRepositories!!.NotificationListMutableData
        ProfileLiveData = apiRepositories!!.ProfileDataMutableData
        CallHistoryLiveData = apiRepositories!!.CallHistoryLiveData
        GetCallHistoryCountLiveData = apiRepositories!!.GetCallHistoryCountLiveData
        CustomerInfoLiveData = apiRepositories!!.CustomerViewInfoLiveData
        UpdateCustomerInfoLiveData = apiRepositories!!.UpdateCustomerDataMutablelivedata
        BlockCustomerCustomerLiveData = apiRepositories!!.BlockCustomerMutableLiveData
        CallCustomerLiveData = apiRepositories!!.CallCustomerMutableLiveData
        CallStatusAvailabilityType = apiRepositories!!.CallStatusAvailableTypeMutableLiveData
        GetUpdateFollowUpMutableLiveData = apiRepositories!!.GetFollowUpDetailsMutableData
        GetFollowUpStatusMutableLiveData = apiRepositories!!.UpdateFollowUpLiveData
        UpdateCallStatusAvailMutableLiveData = apiRepositories!!.UpdateCallStatusLiveData
        DeviceTokenMutableLiveData = apiRepositories!!.DeviceTokenMutableLiveData
        GetCallListCountLiveData = apiRepositories!!.GetCallCountLiveData
        GetShareDocumentMutableLiveData = apiRepositories!!.GetShareDocumentLiveData
        SubmitShareDocumentLiveData = apiRepositories!!.SendWhatsAppDocumentLiveData
        NotificationTotalCountLiveData = apiRepositories!!.GetNotificationTotalCountLiveData
        TemplatesLiveData = apiRepositories!!.TemplatesMutableData
    }


    fun getTemplate( activity: Activity?, token: String) {
        apiRepositories!!.GetTemplates(activity,token)
    }


    fun getCallsbytype(limit: Int, offset: Int, keyword: String, type: String, token: String,count: Int,loginId: Int, activity: Activity?) {
        apiRepositories!!.GetCallsListByType(limit, offset,keyword, type, token, count,loginId,activity)
    }

    fun getNotificationList(
        limit: Int,
        offset: Int,
        keyword: Int,
        count: Int,
        login_id: Int,
        token: String,
        activity: Activity?
    ) {
        apiRepositories!!.GetNotification(limit, offset, keyword, count, login_id, token, activity)
    }

    fun getCallTotalCount(
        count: Int,
        type: String,
        loginId: Int,
        token: String,
        activity: Activity?
    ) {
        apiRepositories!!.GetCallCount(count, type, loginId, token,activity)
    }

    fun getProfileData(agent_id: Int, token: String, activity: Activity?) {
        apiRepositories!!.GetProfileData(agent_id, token, activity)
    }

    fun geteCallHistoryLiveData(
        limit: Int,
        offset: Int,
        keyword: String,
        count: Int,
        customer_id: Int,
        agent_id: Int,
        token: String,
        activity: Activity?
    ) {
        apiRepositories!!.GetCallHistoryList(
            limit,
            offset,
            keyword,
            count,
            customer_id,
            agent_id,
            token,
            activity
        )
    }

    fun getCallHistoryCount(
        limit: Int,
        offset: Int,
        keyword: String,
        count: Int,
        customer_id: Int,
        agent_id: Int,
        token: String,
        activity: Activity?

    ) {
        apiRepositories!!.GetCallHistoryCount(
            limit,offset,keyword,customer_id,agent_id,
            count,
            token,
            activity
        )
    }

    fun getCustomerInfo(customerId: Int, LoginId: Int, token: String, activity: Activity?) {
        apiRepositories!!.GetCustomerViewInfo(customerId, LoginId, token, activity)
    }

    fun updateCustomerInfo(jsonObject: JsonObject, token: String, activity: Activity?) {
        apiRepositories!!.UpdateCustomerInfo(jsonObject, token, activity)
    }

    fun blockCustomer(jsonObject: JsonObject,token:String,activity: Activity?) {
        apiRepositories!!.BlockCustomer(jsonObject, token,activity)
    }

    fun callCustomer(jsonObject: JsonObject, token: String, activity: Activity?) {
        apiRepositories!!.CallCustomer(jsonObject, token, activity)
    }

    fun callStatusAvailability(token: String, activity: Activity?) {
        apiRepositories!!.CallStatusAvailability(token, activity)
    }

    fun getFollowUpDetails(
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
        apiRepositories!!.GetFollowUpDetails(
            limit,
            offset,
            keyword,
            count,
            fromdate,
            todate,
            login_id,
            token,
            activity
        )
    }

    fun updateFollowUpStatus(jsonObject: JsonObject, token: String, activity: Activity?) {
        apiRepositories!!.UpdateFollowCompleted(jsonObject, token, activity)
    }

    fun updateCallStatusAvailability(jsonObject: JsonObject, token: String, activity: Activity?) {
        apiRepositories!!.UpdateCallStatusAvailablity(jsonObject, token, activity)
    }


    fun updateDeviceToken(jsonObject: JsonObject, activity: Activity?) {
        apiRepositories!!.DeviceToken(jsonObject, activity)
    }

    fun getShareDocumentList(projectid:Int, token:String ,activity: Activity?) {
        apiRepositories!!.GetShareDocuments(projectid,token, activity)
    }

    fun SubmitShareDocumentWhatApp(jsonObject: JsonObject, token:String ,activity: Activity?) {
        apiRepositories!!.SubmitDocumentWhatsApp(jsonObject,token, activity)
    }

    fun getTotalNotificaionCount(count: Int, login_id: Int, token: String, activity: Activity??) {
        apiRepositories!!.GetNotificationTotalCount(count, login_id,token, activity)
    }
}