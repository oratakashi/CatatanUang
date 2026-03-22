package com.oratakashi.catatanuang.domain.usecase

import com.oratakashi.catatanuang.domain.model.Transaksi
import com.oratakashi.catatanuang.domain.repository.ITransaksiRepository

/**
 * Use case for retrieving a single financial transaction by its ID.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property repository The transaction repository.
 */
class GetTransaksiByIdUseCase(private val repository: ITransaksiRepository) {

    /**
     * Returns the [Transaksi] with the given [id], or null if not found.
     *
     * @param id The unique identifier of the transaction.
     */
    suspend operator fun invoke(id: Int): Transaksi? = repository.getTransaksiById(id)
}

