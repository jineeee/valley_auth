package com.example.auth_client.auth

import NetworkServiceImpl
import android.util.Log
import androidx.annotation.Nullable
import com.example.auth_client.Application
import com.example.auth_client.data.model.DefaultResponse
import com.example.auth_client.data.pref.SharedPreferenceController
import com.google.gson.JsonObject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator : Authenticator {
    @Nullable
    override fun authenticate(route: Route?, response: Response?): Request? {
        val accessToken: String =
            SharedPreferenceController.getAuthorization(Application.getApplicationContext())

        if (!isRequestWithAccessToken(response!!) || accessToken.isEmpty()) {
            return null
        }

        synchronized(this) {
            val newAccessToken: String =
                SharedPreferenceController.getAuthorization(Application.getApplicationContext())
            if (accessToken != newAccessToken) {
                return newRequestWithAccessToken(response.request(), newAccessToken)
            }

            // refresh token으로 새 access token 요청
            val refreshToken: String =
                SharedPreferenceController.getRefreshAuthorization(Application.getApplicationContext())
            val refreshObject = JsonObject()
            refreshObject.addProperty("refreshToken", refreshToken)
            val tokenResponse: retrofit2.Response<DefaultResponse> =
                NetworkServiceImpl.SERVICE.getNewToken(refreshObject).execute()

            if (tokenResponse.isSuccessful) {
                val userToken = tokenResponse.body() ?: return null
                SharedPreferenceController.setAuthorization(
                    Application.getApplicationContext(),
                    userToken.data!!
                )

                // 새 access token으로 origin request 요청
                return newRequestWithAccessToken(response.request(), userToken.data)
            } else {
                if (tokenResponse.code() == 401) {
                    Log.e("AUTH LOGOUT", tokenResponse.toString())
                }
            }
            return null
        }
    }

    private fun isRequestWithAccessToken(response: Response): Boolean {
        val header = response.request().header("Authorization")
        return header != null && header.startsWith("Bearer")
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String): Request? {
        return request.newBuilder()
            .removeHeader("Authorization")
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}