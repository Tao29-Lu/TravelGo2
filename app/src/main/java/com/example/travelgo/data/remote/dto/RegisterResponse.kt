package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val message: String,
    @SerializedName("_id")
    val id: String? = null,
    val username: String? = null,
    val nombre: String? = null,
    val email: String? = null,
    val token: String? = null
)