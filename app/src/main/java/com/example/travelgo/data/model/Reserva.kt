package com.example.travelgo.data.model
import com.google.gson.annotations.SerializedName
data class Reserva(
    @SerializedName("_id")
    val id: String = "",
    val clienteId: String = "",
    val paqueteId: String = "",
    val fechaReserva: Long = System.currentTimeMillis(),
    val fechaInicio: Long = 0L,
    val fechaFin: Long = 0L,
    val numeroPersonas: Int = 1,
    val precioTotal: Double = 0.0,
    val estado: EstadoReserva = EstadoReserva.PENDIENTE,
    val notas: String = "",
    val fotoClienteUri: String = "", // Foto tomada con la cámara
    val itinerarioId: String = ""
)

enum class EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    PAGADA,
    EN_PROCESO,
    COMPLETADA,
    CANCELADA
}