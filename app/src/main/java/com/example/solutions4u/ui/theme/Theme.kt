package com.example.solutions4u.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val Solutions4UColorScheme = lightColorScheme(
    primary = Green600,
    onPrimary = White,
    primaryContainer = Green500,
    onPrimaryContainer = White,
    secondary = Red500,
    onSecondary = White,
    secondaryContainer = Red600,
    onSecondaryContainer = White,
    background = Green500,
    onBackground = White,
    surface = White,
    onSurface = Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray
)

@Composable
fun Solutions4UTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = Solutions4UColorScheme,
        typography = Typography,
        content = content
    )
}
