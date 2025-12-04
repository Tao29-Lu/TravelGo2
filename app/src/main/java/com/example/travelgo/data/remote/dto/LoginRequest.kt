package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val username: String, // En la app se llama username, pero se envía como 'email'
    @SerializedName("password")
    val contrasena: String // En la app se llama contrasena, pero se envía como 'password'
)