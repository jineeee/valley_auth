package com.example.auth_client.data.model

data class UserListResponse (
    val status:Int,
    val success:Boolean,
    val message:String,
    val data:List<UserInfo>
)