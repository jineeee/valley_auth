package com.earlyBuddy.earlybuddy_android.data.pref

import android.content.Context

object SharedPreferenceController{

    private val USER_NAME = "MYKEY"
    private val myAuth = "myAuth"
    private val myId = "id"
    private val admin = "admin"
    private val autoLogin = "autoLogin"

    fun setId(context: Context, id: String) {
        val pref =
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(myId, id)
        editor.apply()
    }

    fun getId(context: Context): String {
        val pref =
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(myId, "")!!
    }

    fun setAdmin(context: Context, id: String) {
        val pref =
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(admin, id)
        editor.apply()
    }

    fun getAdmin(context: Context): String {
        val pref =
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(admin, "")!!
    }


    fun setAuthorization(context: Context, authorization: String) {
        val pref =
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(myAuth, authorization)
        editor.apply()
    }

    fun getAuthorization(context: Context): String {
        val pref =
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(myAuth, "")!!
    }

    fun setAutoLogin(context: Context, bool: Boolean){
        val pref = context.getSharedPreferences(autoLogin, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putBoolean(autoLogin, bool)
        editor.apply()
    }

    fun getAutoLogin(context: Context) : Boolean {
        val pref = context.getSharedPreferences(autoLogin, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getBoolean(autoLogin, false)
    }

}