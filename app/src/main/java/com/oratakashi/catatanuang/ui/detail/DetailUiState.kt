package com.oratakashi.catatanuang.ui.detail

import com.oratakashi.catatanuang.domain.model.Transaksi

/**
 * Represents the possible UI states for the Transaction Detail screen.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
sealed class DetailUiState {

    /** Initial loading state while fetching the transaction. */
    data object Loading : DetailUiState()

    /**
     * Successfully loaded the transaction.
     *
     * @property transaksi The loaded [Transaksi].
     */
    data class Success(val transaksi: Transaksi) : DetailUiState()

    /**
     * Failed to load the transaction.
     *
     * @property message A user-facing error message.
     */
    data class Error(val message: String) : DetailUiState()

    /** Deletion completed — the screen should navigate back. */
    data object Deleted : DetailUiState()
}

