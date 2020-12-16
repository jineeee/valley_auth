package com.example.auth_client.data.model

data class DefaultResponse (
    val status:Int,
    val success:Boolean,
    val message:String,
    val data: String?
)