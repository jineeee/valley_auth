package com.example.auth_client

import com.earlyBuddy.earlybuddy_android.data.pref.SharedPreferenceController
import okhttp3.Interceptor
import okhttp3.Response

class CookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request =
            chain.request().newBuilder().header("Content-Type", "application/json")
                .header("token", SharedPreferenceController.getAuthorization(Application.getApplicationContext()))
                .build()
        return chain.proceed(request)
    }
}