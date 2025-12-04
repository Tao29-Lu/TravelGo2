package com.example.travelgo.data.model
import com.google.gson.annotations.SerializedName
data class PaqueteTuristico(
    @SerializedName("_id")
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val destino: String = "",
    val duracionDias: Int = 0,
    val precio: Double = 0.0,
    val tipo: TipoPaquete = TipoPaquete.COMUNITARIO,
    val incluye: List<String> = emptyList(),
    val imagenUrl: String = "",
    val disponible: Boolean = true,
    val certificacionSustentable: Boolean = false
)

enum class TipoPaquete {
    COMUNITARIO,
    ECO_TURISMO,
    AVENTURA,
    CULTURAL,
    GASTRONOMICO
}