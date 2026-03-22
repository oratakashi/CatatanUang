package com.oratakashi.catatanuang.domain.usecase

import com.oratakashi.catatanuang.domain.model.Transaksi
import com.oratakashi.catatanuang.domain.repository.ITransaksiRepository

/**
 * Use case for adding a new financial transaction.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property repository The transaction repository.
 */
class AddTransaksiUseCase(private val repository: ITransaksiRepository) {

    /**
     * Validates and inserts a [Transaksi] into the repository.
     *
     * @param transaksi The transaction to add.
     * @throws IllegalArgumentException if nominal is zero or negative, or judul is blank.
     */
    suspend operator fun invoke(transaksi: Transaksi) {
        require(transaksi.judul.isNotBlank()) { "Judul tidak boleh kosong" }
        require(transaksi.nominal > 0) { "Nominal harus lebih dari 0" }
        repository.insertTransaksi(transaksi)
    }
}

