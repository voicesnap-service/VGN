package com.vsnap.vgn.application.Activities


import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Modal.LoginData
import com.vsnap.vgn.application.Modal.LoginUserDetails
import com.vsnap.vgn.application.Modal.VersionCheckData
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.CommonUtil.HelpUrl
import com.vsnap.vgn.application.Utils.CustomLoading
import com.vsnap.vgn.application.Utils.InAppUpdate
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Auth
import org.mindrot.jbcrypt.BCrypt


class Splash : AppCompatActivity() {
    @JvmField
    @BindView(R.id.lnrSnackBar)
    var lnrSnackBar: LinearLayout? = null

    @JvmField
    @BindView(R.id.lblInstall)
    var lblInstall: TextView? = null
    var handler: Handler? = null
    var progressDialog: ProgressDialog? = null
    var authViewModel: Auth? = null
    var GetLoginData: List<LoginData> = ArrayList()
    var GetVersionCheckData: List<VersionCheckData> = ArrayList()
    var GetUserDetailsData: LoginUserDetails? = null
    var Ver_UpdateAvailable = "0"
    var Force_UpdateReq = "1"
    var VersionCode = 2


    var appUpdateManager: AppUpdateManager? = null
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null
    private val FLEXIBLE_APP_UPDATE_REQ_CODE = 123
    private val IMMEDIATE_APP_UPDATE_REQ_CODE = 124
    private lateinit var inAppUpdate: InAppUpdate

    var ForceUpdate = 0
    var UpdateAvailable: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        progressDialog = CustomLoading.createProgressDialog(this@Splash)
        progressDialog!!.show()

        if (!CommonUtil.isNetworkConnected(this@Splash)) {
            progressDialog!!.dismiss()
            val dlgAlert = AlertDialog.Builder(this@Splash)
            dlgAlert.setMessage(resources.getString(R.string.txt_network))
            dlgAlert.setTitle(resources.getString(R.string.txt_error_msg))
            dlgAlert.setPositiveButton(
                resources.getString(R.string.txt_Ok)
            ) { dialog, which ->
                finish()
                dialog.dismiss()
            }
            dlgAlert.setCancelable(true)
            dlgAlert.create().show()
        } else {
            progressDialog!!.dismiss()
            handler = Handler()
            handler!!.postDelayed({
                progressDialog!!.dismiss()

                var emailid = SharedPreference.getShH_Email(this)
                var password = SharedPreference.getSH_Password(this)
                authViewModel!!.VersionCheck(CommonUtil.DeviceType!!, VersionCode!!, this)

            }, 2000)
        }
        authViewModel = ViewModelProvider(this).get(Auth::class.java)
        authViewModel!!.init()

        authViewModel!!.VersionCheckLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                GetVersionCheckData = response.data!!
                if (status == 1) {
                    Log.d("VersionCheckData", GetVersionCheckData.size.toString())
                    HelpUrl = GetVersionCheckData.get(0).help_link

                    ForceUpdate = GetVersionCheckData.get(0).force_update
                    UpdateAvailable = GetVersionCheckData.get(0).update_available

                    if (UpdateAvailable == 0 && ForceUpdate == 0) {
                        AutoLogin()
                    }
                    else {
                        if (UpdateAvailable == 1 && ForceUpdate == 1) {
                            checkImmediateUpdate()
                        } else if (UpdateAvailable == 1 && ForceUpdate == 0) {
                            checkFlexibleUpdate()
                        }
                    }
                } else {
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                Log.d("loginError", "test")

                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }


        authViewModel!!.LogindetailsLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    GetLoginData = response.data!!
                    SharedPreference.putToken(this, GetLoginData.get(0).token)
                    GetUserDetailsData = GetLoginData.get(0).UserDetails!!
                    CommonUtil.UserData = GetLoginData.get(0).UserDetails!!
                    val i = Intent(this, DashboardHome::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finishAffinity()
                } else {

                    ApiAlert(this, message)
                }
            } else {
                Log.d("loginError", "test")

                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }

    }
    fun ApiAlert(activity: Activity?, msg: String?) {
        if (activity != null) {
            val dlg = androidx.appcompat.app.AlertDialog.Builder(activity)
            dlg.setTitle("Info")
            dlg.setMessage(msg)
            dlg.setPositiveButton("OK") { dialog, which ->

                val i = Intent(this, EnterEmail::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)

            }
            dlg.setCancelable(false)
            dlg.create()
            dlg.show()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun checkImmediateUpdate() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.getAppUpdateInfo()
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startImmediateUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startImmediateUpdateFlow(appUpdateInfo)
            } else {
                AutoLogin()
            }
        }
    }

    private fun checkFlexibleUpdate() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.getAppUpdateInfo()
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                startFlexibleUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else {
                AutoLogin()
            }
        }
    }

    private fun startFlexibleUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                this,
                FLEXIBLE_APP_UPDATE_REQ_CODE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun startImmediateUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                this,
                IMMEDIATE_APP_UPDATE_REQ_CODE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                AutoLogin()
            } else if (resultCode == RESULT_OK) {
            } else {
                checkFlexibleUpdate()
            }
        } else if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                finish()
            } else if (resultCode == RESULT_OK) {
            } else {
                checkImmediateUpdate()
            }
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        lnrSnackBar!!.setVisibility(View.VISIBLE)
        lblInstall!!.setOnClickListener(View.OnClickListener {
            if (appUpdateManager != null) {
                appUpdateManager!!.completeUpdate()
            }
        })
    }

    private fun removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager!!.unregisterListener(installStateUpdatedListener!!)
        }
    }

    override fun onStop() {
        super.onStop()
        removeInstallStateUpdateListener()
    }

    private fun AutoLogin() {
        var emailid = SharedPreference.getShH_Email(this)
        var password = SharedPreference.getSH_Password(this)

            if (emailid.isNullOrEmpty() || password.isNullOrEmpty()) {
                val i = Intent(this, EnterEmail::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                finishAffinity()

            } else {
                var EmailID = SharedPreference.getShH_Email(this)

                var storedpassword = SharedPreference.getSH_Password(this)
                var EncryptedPassword = BCrypt.hashpw(storedpassword, BCrypt.gensalt())
                SharedPreference.putEncryptedPassword(this, EncryptedPassword!!)
                Log.d("EmailIdAutologin",EmailID!!)
                Log.d("PasswordAutologin",EncryptedPassword!!)

                val jsonObject = JsonObject()
                jsonObject.addProperty(ApiRequestNames.Req_EmailID, EmailID)
                jsonObject.addProperty(ApiRequestNames.Req_password, EncryptedPassword)
                authViewModel!!.getUserDetails(jsonObject, this)
            }
    }

}