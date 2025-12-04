package com.example.travelgo.data.remote.dto

data class CreateReservaRequest(
    val paqueteId: String,
    val fechaInicio: Long,
    val fechaFin: Long,
    val numeroPersonas: Int,
    val notas: String
)