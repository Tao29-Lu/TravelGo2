package com.example.travelgo.data.remote.dto

data class RegisterRequest(
    val username: String,
    val password: String,
    val nombre: String,
    val email: String
)