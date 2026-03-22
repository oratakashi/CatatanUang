package com.oratakashi.catatanuang.ui.home

import com.oratakashi.catatanuang.domain.model.KategoriSummary
import com.oratakashi.catatanuang.domain.model.PieChartPeriod

/**
 * Represents the UI state specifically for the pie chart section on the Home screen.
 * Isolated from [HomeUiState] so that period changes only trigger recomposition inside
 * the chart composable, leaving the rest of the screen untouched.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
sealed class PieChartUiState {

    /** Loading state while chart data is being fetched after a period change. */
    data object Loading : PieChartUiState()

    /**
     * Success state containing all data required to render the pie chart.
     *
     * @property totalPemasukan Total income for the selected period.
     * @property totalPengeluaran Total expense for the selected period.
     * @property kategoriList Breakdown of transactions grouped by category.
     * @property selectedPeriod The currently active [PieChartPeriod].
     */
    data class Success(
        val totalPemasukan: Double,
        val totalPengeluaran: Double,
        val kategoriList: List<KategoriSummary>,
        val selectedPeriod: PieChartPeriod
    ) : PieChartUiState()

    /**
     * Error state for the chart section.
     *
     * @property message A user-friendly error message.
     */
    data class Error(val message: String) : PieChartUiState()
}

