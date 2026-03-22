package com.oratakashi.catatanuang

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oratakashi.catatanuang.data.preferences.AppPreferences
import com.oratakashi.catatanuang.domain.model.AppTheme
import com.oratakashi.catatanuang.navigation.AppNavGraph
import com.oratakashi.catatanuang.ui.theme.CatatanUangTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import java.util.Locale

/**
 * Single-activity entry point of the application.
 * Applies the user's chosen language and theme before the UI is rendered.
 *
 * Language is applied at [attachBaseContext] so resource strings are resolved in
 * the correct locale from the very first frame.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
class MainActivity : ComponentActivity() {

    private val prefs: AppPreferences by inject()

    override fun attachBaseContext(newBase: Context) {
        // Read the saved language synchronously — this is the only safe place to apply locale
        // before the context is attached. The blocking call is intentional and short-lived.
        val language = runBlocking { AppPreferences(newBase).language.first() }
        val locale = Locale.forLanguageTag(language.code)
        Locale.setDefault(locale)
        val config = newBase.resources.configuration.apply { setLocale(locale) }
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val theme by prefs.theme.collectAsStateWithLifecycle(initialValue = AppTheme.SYSTEM)
            val isSystemDark = isSystemInDarkTheme()

            val isDark = when (theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemDark
            }

            CatatanUangTheme(darkTheme = isDark) {
                AppNavGraph()
            }
        }
    }
}
