package com.example.solutions4u.ui.theme

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.solutions4u.screens.parseHexColor
import kotlinx.coroutines.flow.map

@Composable
fun Solutions4UTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // Read the saved colour from DataStore reactively
    val savedColorHex by ThemeManager.getBackgroundColor(context)
        .collectAsState(initial = ThemeManager.DEFAULT_COLOR)

    val bgColor = parseHexColor(savedColorHex) ?: Color(0xFF2E7D32)

    val colorScheme = lightColorScheme(
        primary = bgColor,
        onPrimary = White,
        primaryContainer = bgColor,
        onPrimaryContainer = White,
        secondary = Red500,
        onSecondary = White,
        secondaryContainer = Red500,
        onSecondaryContainer = White,
        background = bgColor,
        onBackground = White,
        surface = White,
        onSurface = Black,
        surfaceVariant = LightGray,
        onSurfaceVariant = DarkGray
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}