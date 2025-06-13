package com.example.strmtv.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Colores oscuros tipo Apple TV+
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),              // Texto primario en botones o elementos destacados
    secondary = Color(0xFFAAAAAA),            // Texto secundario o iconos secundarios
    tertiary = Color(0xFF888888),             // Elementos terciarios, chips, etc.
    background = Color(0xFF0F0F0F),            // Fondo general
    surface = Color(0xFF1A1A1A),               // Fondo de tarjetas, barras
    onPrimary = Color.Black,                   // Texto o icono encima de primary
    onSecondary = Color.White,                 // Texto encima de secondary
    onTertiary = Color.White,
    onBackground = Color.White,                // Texto normal
    onSurface = Color.White                    // Texto en tarjetas, botones, etc.
)

// Colores claros (por si el sistema está en light mode, aunque Apple TV+ suele ser dark)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF000000),               // Texto primario en botones
    secondary = Color(0xFF444444),
    tertiary = Color(0xFF666666),
    background = Color(0xFFFFFFFF),            // Fondo claro
    surface = Color(0xFFF0F0F0),               // Fondo de tarjetas claro
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun StrmtvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}