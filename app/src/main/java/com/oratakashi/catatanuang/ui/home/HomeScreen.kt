package com.oratakashi.catatanuang.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oratakashi.catatanuang.R
import com.oratakashi.catatanuang.ui.home.components.BalanceCard
import com.oratakashi.catatanuang.ui.home.components.MonthlyPieChart
import com.oratakashi.catatanuang.ui.home.components.TransaksiItem
import org.koin.androidx.compose.koinViewModel

/**
 * Home screen composable showing balance, pie chart, and 6 latest transactions.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param onNavigateToRecord Callback to navigate to the record finance screen.
 * @param onNavigateToReport Callback to navigate to the report screen.
 * @param onNavigateToSettings Callback to navigate to the settings screen.
 * @param onNavigateToDetail Callback to navigate to the detail screen for a transaction.
 * @param viewModel The [HomeViewModel] injected via Koin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToRecord: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val chartState by viewModel.chartState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onNavigateToReport) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = stringResource(R.string.label_laporan)
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.title_pengaturan)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToRecord) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.label_tambah_transaksi)
                )
            }
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is HomeUiState.Success -> {
                val data = state.data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        BalanceCard(saldo = data.saldo)
                    }
                    item {
                        MonthlyPieChart(
                            chartState = chartState,
                            onPeriodSelected = viewModel::onPeriodChanged
                        )
                    }
                    if (data.latestTransaksi.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.label_transaksi_terbaru),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        items(
                            items = data.latestTransaksi,
                            key = { it.id }
                        ) { transaksi ->
                            TransaksiItem(
                                transaksi = transaksi,
                                onClick = { onNavigateToDetail(transaksi.id) }
                            )
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.label_belum_ada_transaksi),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

