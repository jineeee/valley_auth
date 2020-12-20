package com.example.auth_client.data

import com.example.auth_client.data.model.DefaultResponse
import com.example.auth_client.data.model.SignInResponse
import com.example.auth_client.data.model.UserInfoResponse
import com.example.auth_client.data.model.UserListResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {
    @POST("/user/signup")
    fun postSignUp(
        @Body body: JsonObject
    ): Call<DefaultResponse>

    @POST("/user/signin")
    fun postSignIn(
        @Body body: JsonObject
    ): Call<SignInResponse>

    @GET("/user/{id}")
    fun userVerify(
        @Path("id") id: String
    ): Call<DefaultResponse>

    @POST("/user/findPassword")
    fun findPassword(
        @Body body: JsonObject
    ): Call<DefaultResponse>

    @POST("/user/changePassword")
    fun changePassword(
        @Body body: JsonObject
    ): Call<DefaultResponse>

    @POST("/user/verifyEmail")
    fun emailVerify(
        @Body body: JsonObject
    ): Call<DefaultResponse>

    @GET("/admin")
    fun getUserList(): Call<UserListResponse>

    @PUT("/admin")
    fun putAdmin(): Call<DefaultResponse>

    @PUT("/admin/update")
    fun updateUserInfo(
        @Body body: JsonObject
    ): Call<DefaultResponse>

    @GET("/admin/{id}")
    fun getUserInfo(
        @Path("id") id: String
    ): Call<UserInfoResponse>

}