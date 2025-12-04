package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val accessToken: String,

    @SerializedName("username")
    val username: String?
)