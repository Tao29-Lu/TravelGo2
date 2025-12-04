package com.example.travelgo.repository

import android.content.Context
import android.util.Log
import com.example.travelgo.data.local.SessionManager
import com.example.travelgo.data.remote.ApiService
import com.example.travelgo.data.remote.RetrofitClient
import com.example.travelgo.data.remote.dto.LoginRequest
import com.example.travelgo.data.remote.dto.RegisterRequest
import com.example.travelgo.data.remote.dto.RegisteredUser

class AuthRepository(context: Context) {
    private val sessionManager = SessionManager(context)
    private val apiService: ApiService = RetrofitClient.createPublic().create(ApiService::class.java)

    suspend fun login(username: String, password: String): Result<String> {
        Log.d("AuthRepo", "Iniciando login para: $username")
        return try {
            val request = LoginRequest(username, password)
            Log.d("AuthRepo", "Enviando petición de login...")
            val response = apiService.login(request)

            if (response.isSuccessful) {
                val loginResponse = response.body()
                val token = loginResponse?.accessToken

                if (token != null) {
                    Log.d("AuthRepo", "Login exitoso. Token recibido.")
                    sessionManager.saveAuthToken(token)
                    sessionManager.saveUsername(username)
                    sessionManager.saveLoginState(true)
                    Result.success(token)
                } else {
                    val errorMsg = "La respuesta fue exitosa pero no se recibió token."
                    Log.e("AuthRepo", "Login fallido: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Cuerpo del error vacío"
                Log.e("AuthRepo", "Login fallido. Código: ${response.code()}, Mensaje: ${response.message()}, Cuerpo del error: $errorBody")
                Result.failure(Exception("Usuario o contraseña incorrectos: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepo", "Login fallido por excepción", e)
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun register(username: String, password: String, nombre: String, email: String): Result<RegisteredUser> {
        Log.d("AuthRepo", "Iniciando proceso de registro para: $username")
        try {
            val request = RegisterRequest(username, password, nombre, email)
            Log.d("AuthRepo", "Enviando petición: $request")
            val response = apiService.register(request)

            if (response.isSuccessful) {
                val registerResponse = response.body()

                if (registerResponse != null) {
                    val user = RegisteredUser(
                        id = registerResponse.id ?: "",
                        username = registerResponse.username ?: "",
                        nombre = registerResponse.nombre ?: "",
                        email = registerResponse.email ?: ""
                    )
                    Log.d("AuthRepo", "Registro exitoso para: $user")
                    return Result.success(user)
                } else {
                    val errorMsg = "La respuesta fue exitosa pero el cuerpo es nulo."
                    Log.e("AuthRepo", "Registro fallido: $errorMsg")
                    return Result.failure(Exception(errorMsg))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Cuerpo del error vacío"
                Log.e("AuthRepo", "Registro fallido. Código: ${response.code()}, Mensaje: ${response.message()}, Cuerpo del error: $errorBody")
                return Result.failure(Exception("Error al registrar usuario: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepo", "Registro fallido por excepción", e)
            return Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun logout() {
        sessionManager.logout()
    }

    fun isLoggedIn() = sessionManager.isLoggedIn()
}