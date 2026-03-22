package com.oratakashi.catatanuang.ui.settings

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oratakashi.catatanuang.R
import com.oratakashi.catatanuang.domain.model.AppLanguage
import com.oratakashi.catatanuang.domain.model.AppTheme
import com.oratakashi.catatanuang.ui.settings.components.SettingsOptionGroup
import org.koin.androidx.compose.koinViewModel

/**
 * Settings screen composable allowing the user to switch the app language and theme.
 *
 * Language changes trigger an [Activity.recreate] so [attachBaseContext] re-applies
 * the correct locale from the very first frame — no strings.xml lookup will use the old locale.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param onNavigateBack Callback to pop the back stack.
 * @param viewModel The [SettingsViewModel] injected via Koin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val activity = LocalContext.current as Activity
    val selectedLanguage by viewModel.language.collectAsStateWithLifecycle()
    val selectedTheme by viewModel.theme.collectAsStateWithLifecycle()

    // Track the initial language so we can detect an actual change and recreate.
    var initialLanguage by remember { mutableStateOf<AppLanguage?>(null) }
    LaunchedEffect(selectedLanguage) {
        if (initialLanguage == null) {
            initialLanguage = selectedLanguage
        } else if (initialLanguage != selectedLanguage) {
            activity.recreate()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_pengaturan)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_kembali)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── Language ──────────────────────────────────────────────────
            Text(
                text = stringResource(R.string.label_ganti_bahasa),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            SettingsOptionGroup(
                modifier = Modifier.fillMaxWidth(),
                options = listOf(
                    stringResource(R.string.label_bahasa_indonesia) to (selectedLanguage == AppLanguage.INDONESIAN),
                    stringResource(R.string.label_bahasa_inggris) to (selectedLanguage == AppLanguage.ENGLISH)
                ),
                onOptionSelected = { index ->
                    viewModel.onLanguageChanged(
                        if (index == 0) AppLanguage.INDONESIAN else AppLanguage.ENGLISH
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Theme ─────────────────────────────────────────────────────
            Text(
                text = stringResource(R.string.label_ganti_tema),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            SettingsOptionGroup(
                modifier = Modifier.fillMaxWidth(),
                options = listOf(
                    stringResource(R.string.label_tema_sesuai_device) to (selectedTheme == AppTheme.SYSTEM),
                    stringResource(R.string.label_tema_light) to (selectedTheme == AppTheme.LIGHT),
                    stringResource(R.string.label_tema_dark) to (selectedTheme == AppTheme.DARK)
                ),
                onOptionSelected = { index ->
                    viewModel.onThemeChanged(
                        when (index) {
                            1 -> AppTheme.LIGHT
                            2 -> AppTheme.DARK
                            else -> AppTheme.SYSTEM
                        }
                    )
                }
            )
        }
    }
}
