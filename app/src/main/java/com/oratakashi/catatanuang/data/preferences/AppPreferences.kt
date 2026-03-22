package com.oratakashi.catatanuang.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.oratakashi.catatanuang.domain.model.AppLanguage
import com.oratakashi.catatanuang.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Top-level DataStore delegate scoped to the application context. */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

/**
 * Manages persistent user preferences (language and theme) via DataStore.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param context Application context used to access the DataStore file.
 */
class AppPreferences(private val context: Context) {

    private companion object {
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_THEME = stringPreferencesKey("theme")
    }

    /**
     * A [Flow] that emits the currently saved [AppLanguage].
     * Defaults to [AppLanguage.INDONESIAN] if not yet set.
     */
    val language: Flow<AppLanguage> = context.dataStore.data.map { prefs ->
        val raw = prefs[KEY_LANGUAGE] ?: AppLanguage.INDONESIAN.name
        AppLanguage.entries.firstOrNull { it.name == raw } ?: AppLanguage.INDONESIAN
    }

    /**
     * A [Flow] that emits the currently saved [AppTheme].
     * Defaults to [AppTheme.SYSTEM] if not yet set.
     */
    val theme: Flow<AppTheme> = context.dataStore.data.map { prefs ->
        val raw = prefs[KEY_THEME] ?: AppTheme.SYSTEM.name
        AppTheme.entries.firstOrNull { it.name == raw } ?: AppTheme.SYSTEM
    }

    /**
     * Persists the selected [AppLanguage].
     *
     * @param language The language to save.
     */
    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { prefs -> prefs[KEY_LANGUAGE] = language.name }
    }

    /**
     * Persists the selected [AppTheme].
     *
     * @param theme The theme to save.
     */
    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { prefs -> prefs[KEY_THEME] = theme.name }
    }
}

