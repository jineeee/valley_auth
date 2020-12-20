package com.example.auth_client.data.pref

import android.content.Context

object SharedPreferenceController{

    private const val USER_NAME = "MYKEY"
    private const val myAuth = "myAuth"
    private const val admin = "admin"
    private const val autoLogin = "autoLogin"

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

    fun deleteAdmin(context: Context) {
        val pref =
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.remove(admin)
        editor.apply()
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

    fun deleteAuthorization(context: Context) {
        val pref =
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.remove(myAuth)
        editor.apply()
    }
}