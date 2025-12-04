package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("users")
    val users: List<UserDto>
)