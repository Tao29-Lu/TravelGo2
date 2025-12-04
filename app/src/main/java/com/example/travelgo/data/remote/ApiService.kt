package com.example.travelgo.data.remote

import com.example.travelgo.data.model.PaqueteTuristico
import com.example.travelgo.data.model.Reserva
import com.example.travelgo.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("api/auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserDto>

    // Users
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: String, @Header("Authorization") token: String): Response<UserDto>

    @GET("api/users")
    suspend fun getAllUsers(@Header("Authorization") token: String): Response<UsersResponse>

    // Paquetes
    @GET("api/paquetes")
    suspend fun getPaquetes(): Response<List<PaqueteTuristico>>

    @GET("api/paquetes/{id}")
    suspend fun getPaqueteById(@Path("id") id: String): Response<PaqueteTuristico>

    // Reservas
    @POST("api/reservas")
    suspend fun createReserva(@Body request: CreateReservaRequest): Response<Reserva>

    @GET("api/reservas/{id}")
    suspend fun getReservaById(@Path("id") id: String): Response<Reserva>

    @PUT("api/reservas/{id}")
    suspend fun updateReserva(@Path("id") id: String, @Body request: UpdateReservaRequest): Response<Unit>
}