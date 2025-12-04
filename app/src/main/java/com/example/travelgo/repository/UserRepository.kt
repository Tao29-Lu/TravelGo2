package com.example.travelgo.repository

import android.content.Context
import com.example.travelgo.data.local.SessionManager
import com.example.travelgo.data.remote.ApiService
import com.example.travelgo.data.remote.RetrofitClient
import com.example.travelgo.data.remote.dto.UserDto

class UserRepository(context: Context) {

    private val apiService: ApiService = RetrofitClient.create(context).create(ApiService::class.java)
    private val sessionManager = SessionManager(context)

    suspend fun getProfile(): Result<UserDto> {
        return try {
            val token = sessionManager.getAuthToken()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getProfile("Bearer $token")

            if (response.isSuccessful) {
                val profile = response.body()

                if (profile != null) {
                    Result.success(profile)
                } else {
                    Result.failure(Exception("Perfil no encontrado"))
                }
            } else {
                Result.failure(Exception("Error al obtener perfil: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}