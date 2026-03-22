package com.oratakashi.catatanuang.ui.record

/**
 * Represents the UI state for the Record Finance screen.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
sealed class RecordUiState {
    /** Idle state — form is ready for input. */
    data object Idle : RecordUiState()

    /** Loading state while the transaction is being saved. */
    data object Loading : RecordUiState()

    /** Success state — transaction was saved successfully. */
    data object Success : RecordUiState()

    /**
     * Error state.
     *
     * @property message A user-friendly error message.
     */
    data class Error(val message: String) : RecordUiState()
}

