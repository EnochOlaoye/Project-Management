package com.example.solutions4u.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

 fun Color.darken(factor: Float = 0.15f): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this.toArgb(), hsl)
    hsl[2] = (hsl[2] - factor).coerceAtLeast(0f)
    return Color(ColorUtils.HSLToColor(hsl))
}

object ThemeManager {
    private val BACKGROUND_COLOR_KEY = stringPreferencesKey("background_color")
    const val DEFAULT_COLOR = "2E7D32" // Green600 equivalent

    fun getBackgroundColor(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[BACKGROUND_COLOR_KEY] ?: DEFAULT_COLOR
        }
    }

    suspend fun saveBackgroundColor(context: Context, hexColor: String) {
        context.dataStore.edit { prefs ->
            prefs[BACKGROUND_COLOR_KEY] = hexColor.removePrefix("#")
        }
    }

    suspend fun getUserColor(context: Context, userId: Int): String {
    val userKey = stringPreferencesKey("user_color_$userId")
    return context.dataStore.data.map { prefs ->
        prefs[userKey] ?: DEFAULT_COLOR
    }.first()
}

suspend fun saveUserColor(context: Context, userId: Int, hexColor: String) {
    val userKey = stringPreferencesKey("user_color_$userId")
    context.dataStore.edit { prefs ->
        prefs[userKey] = hexColor.removePrefix("#")
    }
}
}