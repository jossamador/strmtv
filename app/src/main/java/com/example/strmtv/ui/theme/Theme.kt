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

// 🌙 Tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    background = md_theme_dark_background,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    secondary = md_theme_dark_secondary,
    tertiary = md_theme_dark_tertiary,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    outline = md_theme_dark_outline,
    surfaceTint = md_theme_dark_primary
)

// ☀️ Tema claro
private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    background = md_theme_light_background,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    secondary = md_theme_light_secondary,
    tertiary = md_theme_light_tertiary,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    outline = md_theme_light_outline,
    surfaceTint = md_theme_light_primary
)

@Composable
fun StrmtvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Puedes cambiarlo a true si deseas usar Material You
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}