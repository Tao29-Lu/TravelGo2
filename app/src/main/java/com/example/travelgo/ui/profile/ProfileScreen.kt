package com.example.travelgo.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.travelgo.repository.AuthRepository
import com.example.travelgo.repository.UserRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val authRepository = remember { AuthRepository(context) }
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userRepository, authRepository))

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    // Observa el estado de logout y navega si es exitoso
    LaunchedEffect(uiState.logoutSuccess) {
        if (uiState.logoutSuccess) {
            navController.navigate("login") {
                // Limpia todo el historial de navegación para que el usuario no pueda volver atrás
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = uiState.error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadProfile() }) {
                            Text("Reintentar")
                        }
                    }
                }
                uiState.user != null -> {
                    val user = uiState.user!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Información Personal",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                HorizontalDivider()
                                Text("Nombre: ${user.nombre}")
                                Text("Usuario: ${user.username}")
                                Text("Email: ${user.email}")
                                Text("Rol: ${user.rol}")
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Botón de Cerrar Sesión
                        Button(
                            onClick = { viewModel.logout() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Cerrar Sesión")
                        }

                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Volver")
                        }
                    }
                }
            }
        }
    }
}