package com.oratakashi.catatanuang.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oratakashi.catatanuang.domain.model.PieChartPeriod
import com.oratakashi.catatanuang.domain.usecase.GetHomeDataUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen.
 * Fetches the 6 latest transactions and the financial summary for the selected pie chart period.
 *
 * [uiState] holds base screen data (saldo + latest transactions) and only enters Loading once
 * on the initial load. Subsequent period changes only affect [chartState], preventing
 * full-screen recomposition.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property getHomeDataUseCase Use case for retrieving home screen data.
 */
class HomeViewModel(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    /** Observed by the Home screen composable for base screen data. */
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _chartState = MutableStateFlow<PieChartUiState>(PieChartUiState.Loading)

    /**
     * Observed exclusively by the pie chart composable.
     * Only this state is updated when the user switches period — the rest of the screen is unaffected.
     */
    val chartState: StateFlow<PieChartUiState> = _chartState.asStateFlow()

    private var collectJob: Job? = null

    init {
        loadHomeData(PieChartPeriod.BULANAN, isInitialLoad = true)
    }

    /**
     * Called when the user selects a new pie chart period.
     * Only [chartState] enters loading; [uiState] retains its current value.
     *
     * @param period The newly selected [PieChartPeriod].
     */
    fun onPeriodChanged(period: PieChartPeriod) {
        val currentChart = _chartState.value
        if (currentChart is PieChartUiState.Success && currentChart.selectedPeriod == period) return
        loadHomeData(period, isInitialLoad = false)
    }

    /**
     * Starts collecting home data for the given [period].
     *
     * On [isInitialLoad], both [uiState] and [chartState] enter Loading.
     * On subsequent calls (period switch), only [chartState] enters Loading so the
     * rest of the screen does not recompose.
     *
     * @param period The [PieChartPeriod] to load data for.
     * @param isInitialLoad Whether this is the first data fetch after ViewModel creation.
     */
    private fun loadHomeData(period: PieChartPeriod, isInitialLoad: Boolean) {
        collectJob?.cancel()
        if (isInitialLoad) {
            _uiState.value = HomeUiState.Loading
        }
        _chartState.value = PieChartUiState.Loading
        collectJob = viewModelScope.launch {
            getHomeDataUseCase(period)
                .catch { e ->
                    val message = e.message ?: "Terjadi kesalahan"
                    if (isInitialLoad) _uiState.value = HomeUiState.Error(message)
                    _chartState.value = PieChartUiState.Error(message)
                }
                .collect { data ->
                    if (isInitialLoad || _uiState.value !is HomeUiState.Success) {
                        _uiState.value = HomeUiState.Success(data)
                    } else {
                        // Refresh base data (saldo + latest) without affecting chart state
                        val currentSuccess = _uiState.value as HomeUiState.Success
                        _uiState.value = currentSuccess.copy(
                            data = currentSuccess.data.copy(
                                latestTransaksi = data.latestTransaksi,
                                saldo = data.saldo
                            )
                        )
                    }
                    _chartState.value = PieChartUiState.Success(
                        totalPemasukan = data.totalPemasukan,
                        totalPengeluaran = data.totalPengeluaran,
                        kategoriList = data.kategoriList,
                        selectedPeriod = period
                    )
                }
        }
    }
}



