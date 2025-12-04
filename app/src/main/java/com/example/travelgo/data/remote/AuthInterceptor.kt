package com.example.travelgo.data.remote

import com.example.travelgo.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // No añadir el token a las rutas públicas de login y registro
        if (originalRequest.url.encodedPath.contains("signin") || originalRequest.url.encodedPath.contains("signup")) {
            return chain.proceed(originalRequest)
        }

        val token = sessionManager.authToken

        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}