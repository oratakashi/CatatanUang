package com.oratakashi.catatanuang.ui.edit

/**
 * Represents the possible UI states for the Edit Transaction screen.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
sealed class EditUiState {

    /** Idle state, no operation in progress. */
    data object Idle : EditUiState()

    /** Loading state while saving or fetching data. */
    data object Loading : EditUiState()

    /** The update was saved successfully — the screen should navigate back. */
    data object Success : EditUiState()

    /**
     * An error occurred.
     *
     * @property message A user-facing error message.
     */
    data class Error(val message: String) : EditUiState()
}

