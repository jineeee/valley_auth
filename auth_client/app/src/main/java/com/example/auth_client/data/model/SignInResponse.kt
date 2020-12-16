package com.example.auth_client.data.model

data class SignInResponse (
    val status:Int,
    val success:Boolean,
    val message:String,
    val data: Token
)

data class Token(
    val id: String,
    val admin: String,
    val accessToken: String,
    val refreshToken: String
)