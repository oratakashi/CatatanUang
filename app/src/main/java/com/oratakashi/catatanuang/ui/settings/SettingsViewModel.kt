package com.oratakashi.catatanuang.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oratakashi.catatanuang.data.preferences.AppPreferences
import com.oratakashi.catatanuang.domain.model.AppLanguage
import com.oratakashi.catatanuang.domain.model.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the Settings screen.
 * Reads and writes language and theme preferences via [AppPreferences].
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property prefs The DataStore-backed preferences repository.
 */
class SettingsViewModel(private val prefs: AppPreferences) : ViewModel() {

    /** The currently selected [AppLanguage], defaulting to [AppLanguage.INDONESIAN]. */
    val language: StateFlow<AppLanguage> = prefs.language.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppLanguage.INDONESIAN
    )

    /** The currently selected [AppTheme], defaulting to [AppTheme.SYSTEM]. */
    val theme: StateFlow<AppTheme> = prefs.theme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppTheme.SYSTEM
    )

    /**
     * Saves the chosen [AppLanguage]. The change is reflected immediately via the [language] flow.
     *
     * @param language The newly selected language.
     */
    fun onLanguageChanged(language: AppLanguage) {
        viewModelScope.launch { prefs.setLanguage(language) }
    }

    /**
     * Saves the chosen [AppTheme]. The change is reflected immediately via the [theme] flow.
     *
     * @param theme The newly selected theme.
     */
    fun onThemeChanged(theme: AppTheme) {
        viewModelScope.launch { prefs.setTheme(theme) }
    }
}

