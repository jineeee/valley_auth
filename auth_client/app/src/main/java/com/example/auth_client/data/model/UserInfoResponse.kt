package com.example.auth_client.data.model

data class UserInfoResponse(
    val status:Int,
    val success:Boolean,
    val message:String,
    val data:UserInfo
)