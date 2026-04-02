package com.example.solutions4u.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
<<<<<<< HEAD
=======

private val LightColorScheme = lightColorScheme(
    primary = Green600,
    onPrimary = White,
    primaryContainer = Green500,
    onPrimaryContainer = White,
    secondary = Red500,
    onSecondary = White,
    background = Green500,
    onBackground = White,
    surface = White,
    onSurface = Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray
)
>>>>>>> development

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1B5E20),
    onPrimary = White,
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = White,
    secondary = Red500,
    onSecondary = White,
    background = Color(0xFF1B5E20),
    onBackground = White,
    surface = Color(0xFF121212),
    onSurface = White,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = White
)

@Composable
fun Solutions4UTheme(
<<<<<<< HEAD
    backgroundColor: Color = Green500,
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = Green600,
        onPrimary = White,
        primaryContainer = backgroundColor,
        onPrimaryContainer = White,
        secondary = Red500,
        onSecondary = White,
        secondaryContainer = Red600,
        onSecondaryContainer = White,
        background = backgroundColor,
        onBackground = White,
        surface = backgroundColor,
        onSurface = White,
        surfaceVariant = LightGray,
        onSurfaceVariant = DarkGray
    )
=======
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
>>>>>>> development

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}