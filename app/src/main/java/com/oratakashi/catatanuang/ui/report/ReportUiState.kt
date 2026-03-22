package com.oratakashi.catatanuang.ui.report

import com.oratakashi.catatanuang.domain.model.LaporanSummary

/**
 * Represents the UI state for the Report screen.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
sealed class ReportUiState {
    /** Loading state while data is being fetched. */
    data object Loading : ReportUiState()

    /**
     * Success state with laporan data.
     *
     * @property summary The financial summary for the selected period.
     */
    data class Success(val summary: LaporanSummary) : ReportUiState()

    /**
     * Error state.
     *
     * @property message A user-friendly error message.
     */
    data class Error(val message: String) : ReportUiState()
}

