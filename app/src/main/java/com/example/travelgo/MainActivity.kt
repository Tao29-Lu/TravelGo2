package com.example.travelgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.travelgo.repository.TravelGoRepository
import com.example.travelgo.ui.navigation.AppNavigation
import com.example.travelgo.ui.theme.TravelGoTheme
import com.example.travelgo.viewmodel.ReservaViewModel
import com.example.travelgo.viewmodel.ReservaViewModelFactory
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TravelGoTheme {
                TravelGoApp()
            }
        }
    }
}

@Composable
fun TravelGoApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val repository = TravelGoRepository(context) // ← Pasar contexto
    val viewModel: ReservaViewModel = viewModel(
        factory = ReservaViewModelFactory(repository)
    )

    AppNavigation(
        navController = navController,
        viewModel = viewModel
    )
}