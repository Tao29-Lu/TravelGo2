package com.example.travelgo.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelgo.data.model.Cliente
import com.example.travelgo.data.model.EstadoPago
import com.example.travelgo.data.model.EstadoReserva
import com.example.travelgo.data.model.MetodoPago
import com.example.travelgo.data.model.Pago
import com.example.travelgo.data.model.PaqueteTuristico
import com.example.travelgo.data.model.Reserva
import com.example.travelgo.repository.TravelGoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReservaViewModel(private val repository: TravelGoRepository) : ViewModel() {

    private val _paquetes = MutableStateFlow<List<PaqueteTuristico>>(emptyList())
    val paquetes: StateFlow<List<PaqueteTuristico>> = _paquetes.asStateFlow()

    private val _paqueteSeleccionado = MutableStateFlow<PaqueteTuristico?>(null)
    val paqueteSeleccionado: StateFlow<PaqueteTuristico?> = _paqueteSeleccionado.asStateFlow()

    private val _reservaActual = MutableStateFlow<Reserva?>(null)
    val reservaActual: StateFlow<Reserva?> = _reservaActual.asStateFlow()

    private val _clienteActual = MutableStateFlow<Cliente?>(null)
    val clienteActual: StateFlow<Cliente?> = _clienteActual.asStateFlow()

    private val _fotoClienteUri = MutableStateFlow<String?>(null)
    val fotoClienteUri: StateFlow<String?> = _fotoClienteUri.asStateFlow()

    private val _estadoReserva = MutableStateFlow<EstadoReservaUI>(EstadoReservaUI.Inicial)
    val estadoReserva: StateFlow<EstadoReservaUI> = _estadoReserva.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    init {
        cargarPaquetes()
    }

    fun cargarPaquetes() {
        viewModelScope.launch {
            try {
                val paquetes = repository.getPaquetes()
                _paquetes.value = paquetes
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar paquetes: ${e.message}"
            }
        }
    }

    fun cargarReserva(reservaId: String) {
        viewModelScope.launch {
            try {
                val reserva = repository.getReservaById(reservaId)
                _reservaActual.value = reserva
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar reserva: ${e.message}"
            }
        }
    }

    fun seleccionarPaquete(paqueteId: String) {
        viewModelScope.launch {
            try {
                val paquete = repository.getPaqueteById(paqueteId)
                _paqueteSeleccionado.value = paquete
                _estadoReserva.value = EstadoReservaUI.PaqueteSeleccionado
            } catch (e: Exception) {
                _mensajeError.value = "Error al seleccionar paquete: ${e.message}"
            }
        }
    }

    fun guardarDatosCliente(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        rut: String
    ) {
        viewModelScope.launch {
            try {
                if (!validarDatosCliente(nombre, apellido, email, telefono, rut)) {
                    return@launch
                }

                val cliente = Cliente(
                    nombre = nombre,
                    apellido = apellido,
                    email = email,
                    telefono = telefono,
                    rut = rut,
                    fotoPerfilUri = _fotoClienteUri.value ?: ""
                )

                val clienteId = repository.addCliente(cliente)
                _clienteActual.value = cliente.copy(id = clienteId)
                _estadoReserva.value = EstadoReservaUI.DatosClienteGuardados
            } catch (e: Exception) {
                _mensajeError.value = "Error al guardar datos del cliente: ${e.message}"
            }
        }
    }

    fun guardarFotoCliente(uri: Uri) {
        _fotoClienteUri.value = uri.toString()
        _estadoReserva.value = EstadoReservaUI.FotoCapturada
    }

    fun crearReserva(
        fechaInicio: Long,
        fechaFin: Long,
        numeroPersonas: Int,
        notas: String
    ) {
        viewModelScope.launch {
            try {
                val paquete = _paqueteSeleccionado.value
                val cliente = _clienteActual.value

                if (paquete == null || cliente == null) {
                    _mensajeError.value = "Datos incompletos para crear la reserva"
                    return@launch
                }

                if (!validarDatosReserva(fechaInicio, fechaFin, numeroPersonas)) {
                    return@launch
                }

                val precioTotal = paquete.precio * numeroPersonas

                val reserva = Reserva(
                    clienteId = cliente.id,
                    paqueteId = paquete.id,
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin,
                    numeroPersonas = numeroPersonas,
                    precioTotal = precioTotal,
                    notas = notas,
                    fotoClienteUri = _fotoClienteUri.value ?: "",
                    estado = EstadoReserva.PENDIENTE
                )

                val reservaId = repository.addReserva(reserva)
                _reservaActual.value = reserva.copy(id = reservaId)
                _estadoReserva.value = EstadoReservaUI.ReservaCreada
            } catch (e: Exception) {
                _mensajeError.value = "Error al crear la reserva: ${e.message}"
            }
        }
    }

    fun procesarPago(metodoPago: MetodoPago) {
        viewModelScope.launch {
            try {
                val reserva = _reservaActual.value

                if (reserva == null) {
                    _mensajeError.value = "No hay reserva para procesar el pago"
                    return@launch
                }

                val pago = Pago(
                    reservaId = reserva.id,
                    monto = reserva.precioTotal,
                    metodoPago = metodoPago,
                    estado = EstadoPago.APROBADO,
                    referencia = "REF-${System.currentTimeMillis()}"
                )

                repository.addPago(pago)

                val reservaActualizada = reserva.copy(estado = EstadoReserva.PAGADA)
                repository.updateReserva(reservaActualizada)
                _reservaActual.value = reservaActualizada

                _estadoReserva.value = EstadoReservaUI.PagoCompletado
            } catch (e: Exception) {
                _mensajeError.value = "Error al procesar el pago: ${e.message}"
            }
        }
    }

    fun reiniciarReserva() {
        _paqueteSeleccionado.value = null
        _reservaActual.value = null
        _clienteActual.value = null
        _fotoClienteUri.value = null
        _estadoReserva.value = EstadoReservaUI.Inicial
        _mensajeError.value = null
    }

    fun limpiarError() {
        _mensajeError.value = null
    }

    private fun validarDatosCliente(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        rut: String
    ): Boolean {
        when {
            nombre.isBlank() -> {
                _mensajeError.value = "El nombre es obligatorio"
                return false
            }
            apellido.isBlank() -> {
                _mensajeError.value = "El apellido es obligatorio"
                return false
            }
            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _mensajeError.value = "El email no es válido"
                return false
            }
            telefono.isBlank() -> {
                _mensajeError.value = "El teléfono es obligatorio"
                return false
            }
            rut.isBlank() -> {
                _mensajeError.value = "El RUT es obligatorio"
                return false
            }
            _fotoClienteUri.value == null -> {
                _mensajeError.value = "Debe capturar una foto del cliente"
                return false
            }
        }
        return true
    }

    private fun validarDatosReserva(
        fechaInicio: Long,
        fechaFin: Long,
        numeroPersonas: Int
    ): Boolean {
        when {
            fechaInicio <= System.currentTimeMillis() -> {
                _mensajeError.value = "La fecha de inicio debe ser futura"
                return false
            }
            fechaFin <= fechaInicio -> {
                _mensajeError.value = "La fecha de fin debe ser posterior a la de inicio"
                return false
            }
            numeroPersonas < 1 -> {
                _mensajeError.value = "Debe haber al menos una persona"
                return false
            }
        }
        return true
    }
}

class ReservaViewModelFactory(private val repository: TravelGoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class EstadoReservaUI {
    object Inicial : EstadoReservaUI()
    object PaqueteSeleccionado : EstadoReservaUI()
    object FotoCapturada : EstadoReservaUI()
    object DatosClienteGuardados : EstadoReservaUI()
    object ReservaCreada : EstadoReservaUI()
    object PagoCompletado : EstadoReservaUI()
}