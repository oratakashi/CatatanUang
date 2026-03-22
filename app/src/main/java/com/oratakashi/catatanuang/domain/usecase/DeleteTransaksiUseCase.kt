package com.oratakashi.catatanuang.domain.usecase

import com.oratakashi.catatanuang.domain.repository.ITransaksiRepository

/**
 * Use case for deleting a financial transaction by its ID.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property repository The transaction repository.
 */
class DeleteTransaksiUseCase(private val repository: ITransaksiRepository) {

    /**
     * Deletes a transaction identified by [id].
     *
     * @param id The unique identifier of the transaction to delete.
     */
    suspend operator fun invoke(id: Int) {
        repository.deleteTransaksiById(id)
    }
}

