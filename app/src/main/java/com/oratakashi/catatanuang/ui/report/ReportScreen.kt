package com.oratakashi.catatanuang.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oratakashi.catatanuang.R
import com.oratakashi.catatanuang.helpers.formatRupiah
import com.oratakashi.catatanuang.ui.home.components.TransaksiItem
import org.koin.androidx.compose.koinViewModel

private val TAB_TITLES = listOf(
    R.string.label_harian,
    R.string.label_mingguan,
    R.string.label_bulanan
)

/**
 * Report screen composable with daily, weekly, and monthly tabs backed by [HorizontalPager]
 * for smooth swipe navigation.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param onNavigateBack Callback to navigate back.
 * @param onNavigateToDetail Callback to navigate to the detail screen for a transaction.
 * @param viewModel The [ReportViewModel] injected via Koin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: ReportViewModel = koinViewModel()
) {
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()
    val harianState by viewModel.harianState.collectAsStateWithLifecycle()
    val mingguanState by viewModel.mingguanState.collectAsStateWithLifecycle()
    val bulananState by viewModel.bulananState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(initialPage = selectedTabIndex) { TAB_TITLES.size }

    // Keep TabRow in sync when user swipes the pager
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onTabSelected(page)
        }
    }

    // Keep pager in sync when user taps a tab
    LaunchedEffect(selectedTabIndex) {
        if (pagerState.currentPage != selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.label_laporan)) },
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
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                TAB_TITLES.forEachIndexed { index, titleRes ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { viewModel.onTabSelected(index) },
                        text = { Text(stringResource(titleRes)) }
                    )
                }
            }

            val pageStates = listOf(harianState, mingguanState, bulananState)

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ReportPage(
                    uiState = pageStates[page],
                    onNavigateToDetail = onNavigateToDetail
                )
            }
        }
    }
}

/**
 * A single pager page that renders the content for a given [ReportUiState].
 *
 * @param uiState The current state for this page.
 * @param onNavigateToDetail Callback to navigate to the detail screen for a transaction.
 */
@Composable
private fun ReportPage(
    uiState: ReportUiState,
    onNavigateToDetail: (Int) -> Unit
) {
    when (val state = uiState) {
        is ReportUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ReportUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is ReportUiState.Success -> {
            val summary = state.summary
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SummaryCard(
                        totalPemasukan = summary.totalPemasukan,
                        totalPengeluaran = summary.totalPengeluaran,
                        saldo = summary.saldo
                    )
                }
                if (summary.transaksiList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.label_belum_ada_transaksi),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(
                        items = summary.transaksiList,
                        key = { it.id }
                    ) { transaksi ->
                        TransaksiItem(
                            transaksi = transaksi,
                            onClick = { onNavigateToDetail(transaksi.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Summary card showing total income, expense, and net balance for a period.
 *
 * @param totalPemasukan Total income.
 * @param totalPengeluaran Total expense.
 * @param saldo Net balance.
 */
@Composable
private fun SummaryCard(
    totalPemasukan: Double,
    totalPengeluaran: Double,
    saldo: Double
) {
    val saldoColor = if (saldo >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.label_pemasukan),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = totalPemasukan.formatRupiah(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.label_pengeluaran),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = totalPengeluaran.formatRupiah(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFF44336),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.label_saldo),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = saldo.formatRupiah(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = saldoColor
                )
            }
        }
    }
}

