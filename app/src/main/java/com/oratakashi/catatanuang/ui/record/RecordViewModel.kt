package com.oratakashi.catatanuang.ui.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oratakashi.catatanuang.domain.model.Transaksi
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import com.oratakashi.catatanuang.domain.usecase.AddTransaksiUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel for the Record Finance screen.
 * Manages form state and delegates saving to [AddTransaksiUseCase].
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property addTransaksiUseCase Use case for adding a new transaction.
 */
class RecordViewModel(
    private val addTransaksiUseCase: AddTransaksiUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecordUiState>(RecordUiState.Idle)

    /** Observed by the Record screen composable. */
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    /**
     * Saves a new [Transaksi] after validation.
     *
     * @param judul The transaction title.
     * @param nominal The transaction amount.
     * @param kategori The transaction category.
     * @param catatan Optional description.
     * @param tipe The transaction type (income or expense).
     * @param tanggal The transaction date.
     */
    fun saveTransaksi(
        judul: String,
        nominal: Double,
        kategori: String,
        catatan: String,
        tipe: TransaksiTipe,
        tanggal: LocalDate
    ) {
        viewModelScope.launch {
            _uiState.value = RecordUiState.Loading
            try {
                addTransaksiUseCase(
                    Transaksi(
                        judul = judul,
                        nominal = nominal,
                        kategori = kategori,
                        tipe = tipe,
                        catatan = catatan,
                        tanggal = tanggal
                    )
                )
                _uiState.value = RecordUiState.Success
            } catch (e: Exception) {
                _uiState.value = RecordUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    /** Resets the UI state back to [RecordUiState.Idle]. */
    fun resetState() {
        _uiState.value = RecordUiState.Idle
    }
}

