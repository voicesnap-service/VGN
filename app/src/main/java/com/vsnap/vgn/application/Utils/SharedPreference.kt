package com.vsnap.vgn.application.Utils

import android.app.Activity
import android.content.Context

object SharedPreference {
    const val SH_PREF = "Mypref"
    const val SH_FirstUser = "UserPref"
    const val SH_Password = "Password"
    const val SH_Email = "Mail"
    const val SH_Token = "token"
    const val SH_CustomerID = "customerid"
    const val SH_AgentID = "agentID"
    const val SH_UserFirst = "login"
    const val SH_Encryptpassword = "Encrypt"

    fun putPassword(activity: Context,password:String) {
        val sharepref = activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
        val ed = sharepref.edit()
        ed.putString(SH_Password, password)
        ed.apply()
        ed.commit()
        return
    }

    fun putEmailID(activity: Context, email:String) {
        val sharepref = activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
        val ed = sharepref.edit()
        ed.putString(SH_Email, email)
        ed.apply()
        ed.commit()
        return
    }


    fun putEncryptedPassword(activity: Context,encryptpassword:String) {
        val sharepref = activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
        val ed = sharepref.edit()
        ed.putString(SH_Encryptpassword, encryptpassword)
        ed.apply()
        ed.commit()
        return
    }
    fun getSH_Encryptedpassword(activity: Activity): String? {
        return activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
            .getString(SH_Encryptpassword, "")
    }
    fun getShH_Email(activity: Activity): String? {
        return activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
            .getString(SH_Email, "")
    }

    fun getSH_Password(activity: Activity): String? {
        return activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
            .getString(SH_Password, "")
    }

    fun clearShLogin(activity: Activity) {
        val sharepref = activity.getSharedPreferences(SH_PREF, 0)
        val ed = sharepref.edit()
        ed.clear()
        ed.apply()
        ed.commit()
        return
    }

    fun putToken(activity: Activity, value: String?) {
        val prefs = activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
        val ed = prefs.edit()
        ed.putString(SharedPreference.SH_Token, value)
        ed.commit()
    }

    fun getSHToken(activity: Activity): String? {
        return activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
            .getString(SharedPreference.SH_Token, "")
    }

    fun putCustomerDetails(activity: Activity, customerId:String,agentID:String) {
        val sharepref = activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
        val ed = sharepref.edit()
        ed.putString(SH_CustomerID, customerId)
        ed.putString(SH_AgentID, agentID)
        ed.apply()
        ed.commit()
        return
    }

    fun getCustomerID(activity: Activity): String? {
        return activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
            .getString(SharedPreference.SH_CustomerID, "")
    }

    fun getAgentID(activity: Activity): String? {
        return activity.getSharedPreferences(SH_PREF, Context.MODE_PRIVATE)
            .getString(SharedPreference.SH_AgentID, "")
    }

    fun putLoginPref(activity: Context, logout:Boolean) {
        val sharepref = activity.getSharedPreferences(SH_FirstUser, Context.MODE_PRIVATE)
        val ed = sharepref.edit()
        ed.putBoolean(SH_UserFirst, logout)
        ed.apply()
        ed.commit()
        return
    }

    fun getSH_LoginPref(activity: Activity): Boolean {
        return activity.getSharedPreferences(SH_FirstUser, Context.MODE_PRIVATE)
            .getBoolean(SH_UserFirst, false)
    }

}