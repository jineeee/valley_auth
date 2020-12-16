package com.example.auth_client.data

import com.example.auth_client.data.model.DefaultResponse
import com.example.auth_client.data.model.SignInResponse
import com.example.auth_client.data.model.UserListResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkService {
    @POST("/user/signup")
    fun postSignUp(
        @Body body: JsonObject
    ): Call<DefaultResponse>

    @POST("/user/signin")
    fun postSignIn(
        @Body body: JsonObject
    ): Call<SignInResponse>

    @POST("/user/emailVerify")
    fun emailVerify(
        @Body body: JsonObject
    ): Call<DefaultResponse>

    @GET("/admin")
    fun getUserList(): Call<UserListResponse>

    @POST("/admin")
    fun postAdmin(
        @Body body: JsonObject
    ): Call<DefaultResponse>

    @POST("/admin/update")
    fun updateUserInfo(
        @Body body: JsonObject
    ): Call<DefaultResponse>

}