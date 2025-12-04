package com.example.travelgo.data.model

data class Pago(
    val id: String = "",
    val reservaId: String = "",
    val monto: Double = 0.0,
    val metodoPago: MetodoPago = MetodoPago.TARJETA_CREDITO,
    val fechaPago: Long = System.currentTimeMillis(),
    val estado: EstadoPago = EstadoPago.PENDIENTE,
    val referencia: String = "",
    val comprobante: String = ""
)

enum class MetodoPago {
    TARJETA_CREDITO,
    TARJETA_DEBITO,
    TRANSFERENCIA,
    EFECTIVO,
    WEBPAY
}

enum class EstadoPago {
    PENDIENTE,
    PROCESANDO,
    APROBADO,
    RECHAZADO,
    REEMBOLSADO
}