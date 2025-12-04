package com.example.travelgo.data.model

data class Itinerario(
    val id: String = "",
    val paqueteId: String = "",
    val descripcion: String = "",
    val actividades: List<String> = emptyList()
)