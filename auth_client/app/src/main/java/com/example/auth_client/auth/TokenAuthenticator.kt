package com.example.auth_client.auth

import android.util.Log
import androidx.annotation.Nullable
import com.example.auth_client.Application
import com.example.auth_client.data.pref.SharedPreferenceController
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator : Authenticator {
    @Nullable
    override fun authenticate(route: Route?, response: Response?): Request? {
        Log.e("AUTHENTICATOR ", response.toString())

        val accessToken: String = SharedPreferenceController.getAuthorization(Application.getApplicationContext())

        if (!isRequestWithAccessToken(response!!) || accessToken.isEmpty()) {
            return null
        }

        synchronized(this) {
            val newAccessToken: String = SharedPreferenceController.getAuthorization(Application.getApplicationContext())
            if (accessToken != newAccessToken) {
                return newRequestWithAccessToken(response.request(), newAccessToken)
            }

            // Need to refresh an access token
            val updatedAccessToken: String =  SharedPreferenceController.getRefreshAuthorization(Application.getApplicationContext())
            return newRequestWithAccessToken(response.request(), updatedAccessToken)
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