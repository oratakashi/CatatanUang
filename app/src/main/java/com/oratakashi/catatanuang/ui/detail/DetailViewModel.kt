package com.oratakashi.catatanuang.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oratakashi.catatanuang.domain.usecase.DeleteTransaksiUseCase
import com.oratakashi.catatanuang.domain.usecase.GetTransaksiByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Transaction Detail screen.
 * Loads a single transaction and handles deletion.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property getTransaksiByIdUseCase Use case for loading a transaction by ID.
 * @property deleteTransaksiUseCase Use case for deleting a transaction.
 */
class DetailViewModel(
    private val getTransaksiByIdUseCase: GetTransaksiByIdUseCase,
    private val deleteTransaksiUseCase: DeleteTransaksiUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)

    /** Observed by the Detail screen composable. */
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    /**
     * Loads the transaction with the given [id].
     *
     * @param id The unique identifier of the transaction.
     */
    fun loadTransaksi(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val transaksi = getTransaksiByIdUseCase(id)
                _uiState.value = if (transaksi != null) {
                    DetailUiState.Success(transaksi)
                } else {
                    DetailUiState.Error("Transaksi tidak ditemukan")
                }
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    /**
     * Deletes the transaction with the given [id] and emits [DetailUiState.Deleted] on success.
     *
     * @param id The unique identifier of the transaction to delete.
     */
    fun deleteTransaksi(id: Int) {
        viewModelScope.launch {
            try {
                deleteTransaksiUseCase(id)
                _uiState.value = DetailUiState.Deleted
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Gagal menghapus transaksi")
            }
        }
    }
}

