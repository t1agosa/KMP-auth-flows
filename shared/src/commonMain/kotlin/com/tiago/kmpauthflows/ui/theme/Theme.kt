package com.tiago.kmpauthflows.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta inspirada en Rolex — verde oscuro, dorado, crema
private val RolexGreen = Color(0xFF0B3D2E)        // el verde característico
private val RolexGreenLight = Color(0xFF1B5E45)   // variante más clara, para containers
private val RolexGold = Color(0xFFB08D2B)         // dorado, para acentos secundarios
private val RolexGoldLight = Color(0xFFE8D9A8)    // dorado suave, para containers
private val Cream = Color(0xFFF7F3E8)             // fondo cálido, no blanco puro
private val CreamSurface = Color(0xFFFCFAF3)      // superficie de cards, un poco más clara
private val DeepText = Color(0xFF1A1A1A)          // casi negro, más cálido que un negro puro

private val LightColors = lightColorScheme(
    primary = RolexGreen,
    onPrimary = Cream,
    primaryContainer = RolexGreenLight,
    onPrimaryContainer = Cream,

    secondary = RolexGold,
    onSecondary = DeepText,
    secondaryContainer = RolexGoldLight,
    onSecondaryContainer = RolexGreen,

    background = Cream,
    onBackground = DeepText,

    surface = CreamSurface,
    onSurface = DeepText,
    surfaceVariant = RolexGoldLight,
    onSurfaceVariant = RolexGreen,

    outline = RolexGold,
    outlineVariant = RolexGoldLight,

    error = Color(0xFF8B2E2E) // rojo apagado, coherente con la paleta cálida — no el rojo Material genérico
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF6FBF9C),          // verde Rolex aclarado, para contraste en fondo oscuro
    onPrimary = Color(0xFF0B3D2E),
    primaryContainer = RolexGreenLight,
    onPrimaryContainer = Cream,

    secondary = Color(0xFFD4B85A),        // dorado aclarado, mismo criterio
    onSecondary = Color(0xFF2A2410),
    secondaryContainer = Color(0xFF4A3D1A),
    onSecondaryContainer = RolexGoldLight,

    background = Color(0xFF14231C),       // verde muy oscuro, no negro puro
    onBackground = Cream,

    surface = Color(0xFF1A2E24),
    onSurface = Cream,
    surfaceVariant = Color(0xFF2A3D30),
    onSurfaceVariant = RolexGoldLight,

    outline = Color(0xFFD4B85A),

    error = Color(0xFFCF6679)
)

@Composable
fun KmpAuthFlowsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}