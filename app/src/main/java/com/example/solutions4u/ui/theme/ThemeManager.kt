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
import androidx.datastore.preferences.core.booleanPreferencesKey

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

 fun Color.darken(factor: Float = 0.15f): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this.toArgb(), hsl)
    hsl[2] = (hsl[2] - factor).coerceAtLeast(0f)
    return Color(ColorUtils.HSLToColor(hsl))
}

object ThemeManager {
    private val BACKGROUND_COLOR_KEY = stringPreferencesKey("background_color")
    private val DUE_DATES_KEY = booleanPreferencesKey("due_dates_enabled")
    private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
    const val DEFAULT_COLOR = "2E7D32" // Green600 equivalent
    private val TRACKED_PLANS_KEY = stringPreferencesKey("tracked_plans")
    private val PRICE_ALERTS_KEY = booleanPreferencesKey("price_alerts_enabled")

    fun getPriceAlertsEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[PRICE_ALERTS_KEY] ?: false
        }
    }

    suspend fun savePriceAlertsEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PRICE_ALERTS_KEY] = enabled
        }
    }

    suspend fun saveTrackedPlans(context: Context, plans: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[TRACKED_PLANS_KEY] = plans.joinToString("|")
        }
    }

    fun getTrackedPlans(context: Context): Flow<Set<String>> {
        return context.dataStore.data.map { prefs ->
            val raw = prefs[TRACKED_PLANS_KEY] ?: ""
            if (raw.isBlank()) emptySet() else raw.split("|").toSet()
        }
    }

    fun getNotificationsEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[NOTIFICATIONS_ENABLED_KEY] ?: false
        }
    }

    suspend fun saveNotificationsEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }

    fun getDueDatesEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[DUE_DATES_KEY] ?: false
        }
    }

    suspend fun saveDueDatesEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DUE_DATES_KEY] = enabled
        }
    }

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