package com.oratakashi.catatanuang.ui.home

import com.oratakashi.catatanuang.domain.model.HomeData

/**
 * Represents the UI state for the Home screen.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
sealed class HomeUiState {
    /** Loading state while data is being fetched. */
    data object Loading : HomeUiState()

    /**
     * Success state with home data.
     *
     * @property data The home data containing latest transactions and monthly summary.
     */
    data class Success(val data: HomeData) : HomeUiState()

    /**
     * Error state.
     *
     * @property message A user-friendly error message.
     */
    data class Error(val message: String) : HomeUiState()
}

