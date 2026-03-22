package com.oratakashi.catatanuang.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oratakashi.catatanuang.domain.model.Transaksi
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import com.oratakashi.catatanuang.domain.usecase.GetTransaksiByIdUseCase
import com.oratakashi.catatanuang.domain.usecase.UpdateTransaksiUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel for the Edit Transaction screen.
 * Loads the existing transaction and delegates saving to [UpdateTransaksiUseCase].
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property getTransaksiByIdUseCase Use case for loading a transaction by ID.
 * @property updateTransaksiUseCase Use case for updating a transaction.
 */
class EditViewModel(
    private val getTransaksiByIdUseCase: GetTransaksiByIdUseCase,
    private val updateTransaksiUseCase: UpdateTransaksiUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditUiState>(EditUiState.Idle)

    /** Observed by the Edit screen composable. */
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    private val _existingTransaksi = MutableStateFlow<Transaksi?>(null)

    /** The original transaction data to pre-fill the form. */
    val existingTransaksi: StateFlow<Transaksi?> = _existingTransaksi.asStateFlow()

    /**
     * Loads the transaction with the given [id] to pre-fill the edit form.
     *
     * @param id The unique identifier of the transaction to load.
     */
    fun loadTransaksi(id: Int) {
        viewModelScope.launch {
            _uiState.value = EditUiState.Loading
            try {
                val transaksi = getTransaksiByIdUseCase(id)
                _existingTransaksi.value = transaksi
                _uiState.value = EditUiState.Idle
            } catch (e: Exception) {
                _uiState.value = EditUiState.Error(e.message ?: "Gagal memuat transaksi")
            }
        }
    }

    /**
     * Saves the updated [Transaksi] after validation.
     *
     * @param id The ID of the transaction to update.
     * @param judul The updated title.
     * @param nominal The updated amount.
     * @param kategori The updated category.
     * @param catatan The updated note.
     * @param tipe The updated transaction type.
     * @param tanggal The updated date.
     */
    fun updateTransaksi(
        id: Int,
        judul: String,
        nominal: Double,
        kategori: String,
        catatan: String,
        tipe: TransaksiTipe,
        tanggal: LocalDate
    ) {
        viewModelScope.launch {
            _uiState.value = EditUiState.Loading
            try {
                updateTransaksiUseCase(
                    Transaksi(
                        id = id,
                        judul = judul,
                        nominal = nominal,
                        kategori = kategori,
                        tipe = tipe,
                        catatan = catatan,
                        tanggal = tanggal
                    )
                )
                _uiState.value = EditUiState.Success
            } catch (e: Exception) {
                _uiState.value = EditUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    /** Resets the UI state back to [EditUiState.Idle]. */
    fun resetState() {
        _uiState.value = EditUiState.Idle
    }
}

