package com.example.travelgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelgo.repository.TravelGoRepository
import com.example.travelgo.viewmodel.ReservaViewModel
import com.example.travelgo.viewmodel.ReservaViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaqueteDetalleScreen(
    paqueteId: String,
    onNavigateBack: () -> Unit,
    onReservar: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { TravelGoRepository(context) }
    val viewModel: ReservaViewModel = viewModel(factory = ReservaViewModelFactory(repository))

    // Llama al ViewModel para que cargue los detalles del paquete
    LaunchedEffect(paqueteId) {
        viewModel.seleccionarPaquete(paqueteId)
    }

    // Observa el estado del paquete seleccionado desde el ViewModel
    val paquete by viewModel.paqueteSeleccionado.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(paquete?.nombre ?: "Detalle del Paquete") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (mensajeError != null) {
                Text(text = "Error: $mensajeError", color = MaterialTheme.colorScheme.error)
            } else if (paquete == null) {
                CircularProgressIndicator()
            } else {
                // Muestra los detalles reales del paquete
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(paquete!!.nombre, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(paquete!!.descripcion, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Precio: $${paquete!!.precio}", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = onReservar,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = paquete != null
                    ) {
                        Text("Reservar ahora")
                    }
                }
            }
        }
    }
}