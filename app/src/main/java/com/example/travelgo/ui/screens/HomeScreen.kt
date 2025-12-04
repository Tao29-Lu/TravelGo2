package com.example.travelgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelgo.repository.TravelGoRepository
import com.example.travelgo.viewmodel.ReservaViewModel
import com.example.travelgo.viewmodel.ReservaViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPaquete: (String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { TravelGoRepository(context) }
    val viewModel: ReservaViewModel = viewModel(factory = ReservaViewModelFactory(repository))

    // Observa la lista de paquetes desde el ViewModel
    val paquetes by viewModel.paquetes.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TravelGo") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
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
            } else if (paquetes.isEmpty()) {
                // Muestra un indicador de carga o un mensaje de lista vacía
                Text("No hay paquetes disponibles en este momento.")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(paquetes) { paquete ->
                        Card(
                            onClick = { onNavigateToPaquete(paquete.id) }, // Usa el ID real del paquete
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    paquete.nombre,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(paquete.descripcion)
                                Text("${paquete.duracionDias} días - $${paquete.precio}")
                            }
                        }
                    }
                }
            }
        }
    }
}