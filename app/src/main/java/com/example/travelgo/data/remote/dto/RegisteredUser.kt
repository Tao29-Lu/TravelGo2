package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisteredUser(
    @SerializedName("_id")
    val id: String,
    val username: String,
    val nombre: String,
    val email: String
)