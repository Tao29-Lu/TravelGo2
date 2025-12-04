package com.example.travelgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travelgo.viewmodel.ReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaFormScreen(
    paqueteId: String,
    viewModel: ReservaViewModel,
    onNavigateBack: () -> Unit,
    onReservaExitosa: (String) -> Unit
) {
    var numeroPersonas by remember { mutableStateOf("1") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Reserva") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = numeroPersonas,
                onValueChange = { numeroPersonas = it },
                label = { Text("Número de personas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Lógica de reserva
                    onReservaExitosa("reserva-123")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Reserva")
            }
        }
    }
}