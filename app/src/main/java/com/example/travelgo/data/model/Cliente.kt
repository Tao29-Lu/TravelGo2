package com.example.travelgo.data.model

data class Cliente(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val telefono: String = "",
    val rut: String = "",
    val direccion: String = "",
    val fotoPerfilUri: String = "",
    val fechaRegistro: Long = System.currentTimeMillis()
)