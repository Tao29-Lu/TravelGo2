package com.example.travelgo.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Colores principales - Pasteles con dorado
val DoradoPastel = Color(0xFFE8C87C)          // Dorado suave pastel
val DoradoClaro = Color(0xFFF5E6C8)           // Dorado muy claro
val DoradoMedio = Color(0xFFD4AF6E)           // Dorado medio
val VerdePastel = Color(0xFFA8D5BA)           // Verde menta pastel (turismo sustentable)
val AzulPastel = Color(0xFFADD8E6)            // Azul cielo pastel
val RosaPastel = Color(0xFFF4C2C2)            // Rosa coral pastel
val LilaPastel = Color(0xFFD4C5F9)            // Lila lavanda pastel
val CremaFondo = Color(0xFFFFFBF5)            // Crema cálido
val BlancoSuave = Color(0xFFFFFEFA)           // Blanco cremoso
val GrisTexto = Color(0xFF5A5A5A)             // Gris para texto
val GrisSuave = Color(0xFFE8E8E8)             // Gris muy claro

// Estados - Versiones pastel
val ConfirmadoVerdePastel = Color(0xFF8FD4A0)  // Verde menta suave
val PendienteAmarilloPastel = Color(0xFFFFE5A0) // Amarillo pastel
val CanceladoRosaPastel = Color(0xFFFFB3B3)    // Rosa pastel suave

private val LightColors = lightColorScheme(
    // Colores principales
    primary = DoradoPastel,
    onPrimary = GrisTexto,
    primaryContainer = DoradoClaro,
    onPrimaryContainer = GrisTexto,

    // Colores secundarios
    secondary = VerdePastel,
    onSecondary = GrisTexto,
    secondaryContainer = Color(0xFFD5EFE1),
    onSecondaryContainer = GrisTexto,

    // Colores terciarios
    tertiary = AzulPastel,
    onTertiary = GrisTexto,
    tertiaryContainer = Color(0xFFE3F2FD),
    onTertiaryContainer = GrisTexto,

    // Fondos y superficies
    background = CremaFondo,
    onBackground = GrisTexto,
    surface = BlancoSuave,
    onSurface = GrisTexto,
    surfaceVariant = GrisSuave,
    onSurfaceVariant = GrisTexto,

    // Estados
    error = CanceladoRosaPastel,
    onError = GrisTexto,

    // Bordes y divisores
    outline = Color(0xFFD0D0D0),
    outlineVariant = Color(0xFFE8E8E8)
)

private val DarkColors = darkColorScheme(
    // Colores principales
    primary = DoradoPastel,
    onPrimary = Color(0xFF2B2520),
    primaryContainer = DoradoMedio,
    onPrimaryContainer = BlancoSuave,

    // Colores secundarios
    secondary = VerdePastel,
    onSecondary = Color(0xFF1F2520),
    secondaryContainer = Color(0xFF3A5A45),
    onSecondaryContainer = BlancoSuave,

    // Colores terciarios
    tertiary = AzulPastel,
    onTertiary = Color(0xFF1A2530),
    tertiaryContainer = Color(0xFF2C4A5F),
    onTertiaryContainer = BlancoSuave,

    // Fondos y superficies
    background = Color(0xFF1C1B1F),
    onBackground = BlancoSuave,
    surface = Color(0xFF2B2A2E),
    onSurface = BlancoSuave,
    surfaceVariant = Color(0xFF3A3940),
    onSurfaceVariant = Color(0xFFE0E0E0),

    // Estados
    error = CanceladoRosaPastel,
    onError = Color(0xFF2B1F1F),

    // Bordes y divisores
    outline = Color(0xFF4A4A4A),
    outlineVariant = Color(0xFF3A3A3A)
)

@Composable
fun TravelGoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}