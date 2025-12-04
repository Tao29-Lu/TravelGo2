package com.example.travelgo.repository

import android.content.Context
import com.example.travelgo.data.model.Cliente
import com.example.travelgo.data.model.EstadoReserva
import com.example.travelgo.data.model.Pago
import com.example.travelgo.data.model.PaqueteTuristico
import com.example.travelgo.data.model.Reserva
import com.example.travelgo.data.remote.ApiService
import com.example.travelgo.data.remote.RetrofitClient
import com.example.travelgo.data.remote.dto.CreateReservaRequest
import com.example.travelgo.data.remote.dto.UpdateReservaRequest

class TravelGoRepository(context: Context) {

    private val apiService: ApiService = RetrofitClient.create(context).create(ApiService::class.java)

    suspend fun getPaquetes(): List<PaqueteTuristico> {
        val response = apiService.getPaquetes()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        throw Exception("Error al obtener paquetes: ${response.message()}")
    }

    suspend fun getPaqueteById(id: String): PaqueteTuristico? {
        val response = apiService.getPaqueteById(id)
        if (response.isSuccessful) {
            return response.body()
        }
        throw Exception("Error al obtener paquete: ${response.message()}")
    }

    suspend fun addCliente(cliente: Cliente): String {
        // Los clientes se crean automáticamente con el registro de usuario
        return "CLI-${System.currentTimeMillis()}"
    }

    suspend fun addReserva(reserva: Reserva): String {
        val request = CreateReservaRequest(
            paqueteId = reserva.paqueteId,
            fechaInicio = reserva.fechaInicio,
            fechaFin = reserva.fechaFin,
            numeroPersonas = reserva.numeroPersonas,
            notas = reserva.notas
        )

        val response = apiService.createReserva(request)
        if (response.isSuccessful) {
            return response.body()?.id ?: ""
        }
        throw Exception("Error al crear reserva: ${response.message()}")
    }

    suspend fun getReservaById(id: String): Reserva? {
        val response = apiService.getReservaById(id)
        if (response.isSuccessful) {
            return response.body()
        }
        throw Exception("Error al obtener reserva: ${response.message()}")
    }

    suspend fun updateReserva(reserva: Reserva) {
        val request = UpdateReservaRequest(
            estado = reserva.estado.name
        )

        val response = apiService.updateReserva(reserva.id, request)
        if (!response.isSuccessful) {
            throw Exception("Error al actualizar reserva: ${response.message()}")
        }
    }

    suspend fun addPago(pago: Pago): String {
        // TODO: Implementar endpoint de pagos
        return "PAG-${System.currentTimeMillis()}"
    }
}