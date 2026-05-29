package com.example.cineflow.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CinematicColorScheme = darkColorScheme(
    primary = CineGold,
    onPrimary = CineDarkBackground,
    secondary = CineGoldVariant,
    onSecondary = CineDarkBackground,
    tertiary = CineRed,
    onTertiary = CineTextPrimary,
    background = CineDarkBackground,
    onBackground = CineTextPrimary,
    surface = CineDarkSurface,
    onSurface = CineTextPrimary,
    surfaceVariant = CineDarkSurfaceVariant,
    onSurfaceVariant = CineTextSecondary
)

@Composable
fun CineFlowTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = CinematicColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CineDarkBackground.toArgb()
            window.navigationBarColor = CineDarkBackground.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}