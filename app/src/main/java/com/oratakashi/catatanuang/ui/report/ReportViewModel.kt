package com.oratakashi.catatanuang.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oratakashi.catatanuang.domain.usecase.GetLaporanBulananUseCase
import com.oratakashi.catatanuang.domain.usecase.GetLaporanHarianUseCase
import com.oratakashi.catatanuang.domain.usecase.GetLaporanMingguanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for the Report screen.
 * All three tabs (Harian, Mingguan, Bulanan) are loaded simultaneously so that
 * the `HorizontalPager` can swipe between them without re-fetching on every page change.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property getLaporanHarianUseCase Use case for daily reports.
 * @property getLaporanMingguanUseCase Use case for weekly reports.
 * @property getLaporanBulananUseCase Use case for monthly reports.
 */
class ReportViewModel(
    private val getLaporanHarianUseCase: GetLaporanHarianUseCase,
    private val getLaporanMingguanUseCase: GetLaporanMingguanUseCase,
    private val getLaporanBulananUseCase: GetLaporanBulananUseCase
) : ViewModel() {

    private val _harianState = MutableStateFlow<ReportUiState>(ReportUiState.Loading)

    /** UI state for the Harian (daily) page. */
    val harianState: StateFlow<ReportUiState> = _harianState.asStateFlow()

    private val _mingguanState = MutableStateFlow<ReportUiState>(ReportUiState.Loading)

    /** UI state for the Mingguan (weekly) page. */
    val mingguanState: StateFlow<ReportUiState> = _mingguanState.asStateFlow()

    private val _bulananState = MutableStateFlow<ReportUiState>(ReportUiState.Loading)

    /** UI state for the Bulanan (monthly) page. */
    val bulananState: StateFlow<ReportUiState> = _bulananState.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)

    /** Index of the currently selected tab (0=Harian, 1=Mingguan, 2=Bulanan). */
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    init {
        loadAllReports()
    }

    /**
     * Syncs the selected tab index when the user swipes or taps a tab.
     *
     * @param index Tab index: 0 = Harian, 1 = Mingguan, 2 = Bulanan.
     */
    fun onTabSelected(index: Int) {
        _selectedTabIndex.value = index
    }

    private fun loadAllReports() {
        viewModelScope.launch {
            getLaporanHarianUseCase()
                .catch { e -> _harianState.value = ReportUiState.Error(e.message ?: "Terjadi kesalahan") }
                .collect { summary -> _harianState.value = ReportUiState.Success(summary) }
        }
        viewModelScope.launch {
            getLaporanMingguanUseCase()
                .catch { e -> _mingguanState.value = ReportUiState.Error(e.message ?: "Terjadi kesalahan") }
                .collect { summary -> _mingguanState.value = ReportUiState.Success(summary) }
        }
        viewModelScope.launch {
            getLaporanBulananUseCase()
                .catch { e -> _bulananState.value = ReportUiState.Error(e.message ?: "Terjadi kesalahan") }
                .collect { summary -> _bulananState.value = ReportUiState.Success(summary) }
        }
    }
}

