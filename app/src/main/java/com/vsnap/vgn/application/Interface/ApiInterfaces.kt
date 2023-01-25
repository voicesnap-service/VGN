package com.vsnap.vgn.application.Interface

import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.*
import com.vsnap.vgn.application.Respository.ApiMethods
import com.vsnap.vgn.application.Respository.ApiRequestNames
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterfaces {

    @POST(ApiMethods.verify_Email)
    fun VerifyEmailApi(@Body jsonObject: JsonObject): Call<VerifyEmailResponse?>?

    @POST(ApiMethods.validate_otp)
    fun VerifyOTP(@Body jsonObject: JsonObject): Call<StatusMessageResponse?>?

    @POST(ApiMethods.updatePassword)
    fun PasswordUpdation(@Body jsonObject: JsonObject): Call<StatusMessageResponse?>?

    @GET(ApiMethods.VersionCheck)
    fun VersionCheck(
        @Query(ApiRequestNames.Req_DeviceType) devicetype: String,
        @Query(ApiRequestNames.Req_VersionCode) versionCode: Int,
    ): Call<VersionCheckResponse?>?


    @POST(ApiMethods.changepassword)
    fun ChangePasswordApi(@Body jsonObject: JsonObject): Call<StatusMessageResponse?>?

    @POST(ApiMethods.loginDetails)
    fun UserLoginDetails(@Body jsonObject: JsonObject): Call<LoginResponse?>?



    @GET(ApiMethods.CallListType)
    fun GetCallListByType(
        @Query(ApiRequestNames.Req_limit) AppId: Int,
        @Query(ApiRequestNames.Req_offset) offset: Int,
        @Query(ApiRequestNames.Req_keyword) keyword: String,
        @Query(ApiRequestNames.Req_count) count: Int,
        @Query(ApiRequestNames.Req_Type) type: String,
        @Query(ApiRequestNames.Req_LoginId) loginId: Int,
        @Query(ApiRequestNames.from_date) fromDate: String,
        @Query(ApiRequestNames.to_date) toDate: String,
        @Header(ApiRequestNames.Req_token) token: String,

        ): Call<RecentCallsResponse?>?

    @GET(ApiMethods.GetNotifications)
    fun GetNotificationList(
        @Query(ApiRequestNames.Req_limit) AppId: Int,
        @Query(ApiRequestNames.Req_offset) offset: Int,
        @Query(ApiRequestNames.Req_keyword) keyword: String,
        @Query(ApiRequestNames.Req_count) count: Int,
        @Query(ApiRequestNames.Req_LoginId) offsdfdevdvsdt: Int,
        @Header(ApiRequestNames.Req_token) token: String,
    ): Call<NotificationResponse?>?




    @GET(ApiMethods.customerDetails)
    fun GetCustomerDetails(@Header(ApiRequestNames.Req_token) token: String,
                           @Query(ApiRequestNames.Req_limit) AppId: Int,
                           @Query(ApiRequestNames.Req_offset) offset: Int,
                           @Query(ApiRequestNames.Req_keyword) keyword: String,
                           @Query(ApiRequestNames.Req_count) count: Int,@Query(ApiRequestNames.customer_type) customer_type: String,
                           @Query(ApiRequestNames.from_date) fromDate: String,
                           @Query(ApiRequestNames.to_date) toDate: String,): Call<CustomerDetails?>?


    @GET(ApiMethods.templates)
    fun GetTemplates(@Header(ApiRequestNames.Req_token) token: String, @Query(ApiRequestNames.Req_TemplateType) AppId: String): Call<TemplatesData?>?

    @GET(ApiMethods.templatesTypes)
    fun GetTemplatesTypes(@Header(ApiRequestNames.Req_token) token: String): Call<TemplatesType?>?


    @GET(ApiMethods.customerTypes)
    fun GetCustomerTypes(@Header(ApiRequestNames.Req_token) token: String): Call<CustomerType?>?

    @GET(ApiMethods.clickToCallDialNumbers)
    fun GetDialNumbers(@Header(ApiRequestNames.Req_token) token: String,@Query(ApiRequestNames.Req_CustomerID) AppId: Int): Call<ClickToCallDialNumbers?>?

    @GET(ApiMethods.GetAgentProfile)
    fun GetAgentProfile(
        @Query(ApiRequestNames.Req_AgentID) AppId: Int,
        @Header(ApiRequestNames.Req_token) token: String
    ): Call<ProfileResponse?>?


    @GET(ApiMethods.GetCallHistory)
    fun GetCallHistory(
        @Query(ApiRequestNames.Req_limit) limit: Int,
        @Query(ApiRequestNames.Req_offset) offset: Int,
        @Query(ApiRequestNames.Req_keyword) keyword: String,
        @Query(ApiRequestNames.Req_count) count: Int,
        @Query(ApiRequestNames.Req_CustomerID) customerId: Int,
        @Query(ApiRequestNames.Req_AgentID) agentId: Int,
        @Header(ApiRequestNames.Req_token) token: String
    ): Call<CallHistoryResponse?>?

//   limit=10&offset=1&keyword&count=0&type=recent&login_id=123456

    @GET(ApiMethods.GetCustomerInfo)
    fun GetCustomerInfo(
        @Query(ApiRequestNames.Req_CustomerID) customerid: Int,
        @Query(ApiRequestNames.Req_LoginId) loginId: Int,
        @Header(ApiRequestNames.Req_token) token: String
    ): Call<GetCusomerInfoResponse?>?

    @POST(ApiMethods.UpdateCustomerInformation)
    fun UpdateCustomerData(
        @Body jsonObject: JsonObject,
        @Header(ApiRequestNames.Req_token) token: String
    ): Call<StatusMessageResponse?>?

    @POST(ApiMethods.blockCustomer)
    fun BlockCustomer(@Body jsonObject: JsonObject,
                      @Header(ApiRequestNames.Req_token)token:String): Call<StatusMessageResponse?>?

    @POST(ApiMethods.CallCustomer)
    fun CallCustomer(
        @Body jsonObject: JsonObject,
        @Header(ApiRequestNames.Req_token) token: String,
    ): Call<StatusMessageResponse?>?


    @GET(ApiMethods.CallstatusAvailability)
    fun CallStatusAvailable(@Header(ApiRequestNames.Req_token) token: String): Call<AvailablityStatusResponse?>?

    @GET(ApiMethods.GetFollowUpReport)
    fun GetFollowUpReport(

    @Query(ApiRequestNames.Req_limit) limit: Int,
    @Query(ApiRequestNames.Req_offset) offset: Int,
    @Query(ApiRequestNames.Req_keyword) keyword: String,
    @Query(ApiRequestNames.Req_count) count: Int,
    @Query(ApiRequestNames.Req_FromDate) fromDate: String,
    @Query(ApiRequestNames.Req_ToDate) toDate: String,
    @Query(ApiRequestNames.Req_LoginId) loginId: Int,
    @Header(ApiRequestNames.Req_token)token: String)
    : Call<GetFollowUpDetailsResponse?>?



    @POST(ApiMethods.FollowUpcompleted)
    fun UpdateFollowUpcompleted(
        @Body jsonObject: JsonObject,
        @Header(ApiRequestNames.Req_token) token: String,
    ): Call<StatusMessageResponse?>?


    @POST(ApiMethods.UpdateCallstatusAvailability)
    fun UpdateCallstatusAvailability(
        @Body jsonObject: JsonObject,
        @Header(ApiRequestNames.Req_token) token: String,
    ): Call<StatusMessageResponse?>?

    @POST(ApiMethods.DeviceToken)
    fun DeviceToken(
        @Body jsonObject: JsonObject
    ): Call<StatusMessageResponse?>?

    @GET(ApiMethods.CallListType)
    fun GetCallListCount(
        @Query(ApiRequestNames.Req_count) AppId: Int,
        @Query(ApiRequestNames.Req_Type) type: String,
        @Query(ApiRequestNames.Req_LoginId) loginId: Int,
        @Header(ApiRequestNames.Req_token) token: String,
    ): Call<GetTotalCountResponse?>?


    @GET(ApiMethods.customerDetailsCount)
    fun GetCustomerListCount(
        @Query(ApiRequestNames.Req_count) AppId: Int,
        @Query(ApiRequestNames.Req_Type) type: String,
        @Query(ApiRequestNames.Req_LoginId) loginId: Int,
        @Header(ApiRequestNames.Req_token) token: String,
    ): Call<GetTotalCountResponse?>?

    @GET(ApiMethods.GetNotifications)
    fun GetTotalacountNotificationList(
        @Query(ApiRequestNames.Req_count) count: Int,
        @Query(ApiRequestNames.Req_LoginId) offsdfdevdvsdt: Int,
        @Header(ApiRequestNames.Req_token) token: String,
    ): Call<GetTotalCountResponse?>?

    @GET(ApiMethods.ProjectDocuments)
    fun GetShareDocumentList(
        @Query(ApiRequestNames.Req_ProjectID) AppId: Int,
        @Header(ApiRequestNames.Req_token) token: String,
        ): Call<GetShareDocumentResponse?>?

    @POST(ApiMethods.SendDocuments)
    fun SendWhatsAppDocument(
        @Body jsonObject: JsonObject,
        @Header(ApiRequestNames.Req_token) token: String,
    ): Call<StatusMessageResponse?>?






    @GET(ApiMethods.GetCallHistory)
    fun GetCallHistoryCount(
        @Query(ApiRequestNames.Req_limit) limit: Int,
        @Query(ApiRequestNames.Req_offset) offset: Int,
        @Query(ApiRequestNames.Req_keyword) keyword: String,
        @Query(ApiRequestNames.Req_count) count: Int,
        @Query(ApiRequestNames.Req_CustomerID) customerId: Int,
        @Query(ApiRequestNames.Req_AgentID) agentId: Int,
        @Header(ApiRequestNames.Req_token) token: String
    ): Call<GetTotalCountResponse?>?


    @Streaming
    @GET
    fun downloadFileWithDynamicUrlAsync(@Url fileUrl: String?): Call<ResponseBody?>?

}