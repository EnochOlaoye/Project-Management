package com.example.solutions4u.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

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
}