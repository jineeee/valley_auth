package com.example.auth_client

import com.example.auth_client.data.pref.SharedPreferenceController
import okhttp3.Interceptor
import okhttp3.Response

class CookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SharedPreferenceController.getAuthorization(Application.getApplicationContext())
        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }

//    override fun intercept(chain: Interceptor.Chain): Response {
//        val accessToken =
//            SharedPreferenceController.getAuthorization(Application.getApplicationContext())
//        var request =
//            chain.request().newBuilder().header("Content-Type", "application/json")
//                .header("token", accessToken)
//                .build()
//        val response = chain.proceed(request)
//
//        // access token이 만료된 경우 (401)
//        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//            synchronized(this) {
//                val newAccessToken: String =
//                    SharedPreferenceController.getAuthorization(Application.getApplicationContext())
//
//                // access token을 재발급 받은 경우
//                if (accessToken != newAccessToken) {
//                    request =
//                        chain.request().newBuilder().header("Content-Type", "application/json")
//                            .header("token", newAccessToken)
//                            .build()
//                    return chain.proceed(request)
//                }
//
//                // refreshToken으로 새 access toekn 발급
//                val updatedAccessToken: String =
//                    SharedPreferenceController.getRefreshAuthorization(Application.getApplicationContext())
//                request =
//                    chain.request().newBuilder().header("Content-Type", "application/json")
//                        .header("token", updatedAccessToken)
//                        .build()
//                return chain.proceed(request)
//            }
//        }
//        return response
//    }
}