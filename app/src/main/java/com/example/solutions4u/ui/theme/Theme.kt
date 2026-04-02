package com.example.solutions4u.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Solutions4UTheme(
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}