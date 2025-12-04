package com.example.travelgo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travelgo.data.local.SessionManager
import com.example.travelgo.ui.profile.ProfileScreen
import com.example.travelgo.ui.screens.*
import com.example.travelgo.viewmodel.ReservaViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: ReservaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashDecider(navController)
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToPaquete = { paqueteId ->
                    navController.navigate("paquete/$paqueteId")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        composable("paquete/{paqueteId}") { backStackEntry ->
            val paqueteId = backStackEntry.arguments?.getString("paqueteId") ?: ""
            PaqueteDetalleScreen(
                paqueteId = paqueteId,
                onNavigateBack = { navController.popBackStack() },
                onReservar = {
                    navController.navigate("reserva/$paqueteId")
                }
            )
        }

        composable("reserva/{paqueteId}") { backStackEntry ->
            val paqueteId = backStackEntry.arguments?.getString("paqueteId") ?: ""
            ReservaFormScreen(
                paqueteId = paqueteId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onReservaExitosa = { reservaId ->
                    navController.navigate("confirmacion/$reservaId") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        composable("confirmacion/{reservaId}") { backStackEntry ->
            val reservaId = backStackEntry.arguments?.getString("reservaId") ?: ""
            ConfirmacionReservaScreen(
                reservaId = reservaId,
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("camera") {
            CameraScreen(
                onNavigateBack = { navController.popBackStack() },
                onPhotoTaken = { uri ->
                    navController.popBackStack()
                }
            )
        }

        composable("profile") {
            ProfileScreen(navController = navController)
        }
    }
}

@Composable
private fun SplashDecider(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val sessionManager = SessionManager(context)
                val loggedIn = sessionManager.isLoggedIn().first()
                val token = sessionManager.getAuthToken()

                if (loggedIn && !token.isNullOrEmpty()) {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
}