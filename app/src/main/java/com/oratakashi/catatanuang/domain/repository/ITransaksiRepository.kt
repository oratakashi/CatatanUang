package com.oratakashi.catatanuang.domain.repository

import com.oratakashi.catatanuang.domain.model.Transaksi
import kotlinx.coroutines.flow.Flow

/**
 * Contract for the transaksi repository consumed by the domain layer.
 * Implementations live in the data layer.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
interface ITransaksiRepository {

    /** Returns all transactions ordered by date descending. */
    fun getAllTransaksi(): Flow<List<Transaksi>>

    /**
     * Returns the [limit] most recent transactions.
     *
     * @param limit Maximum number of records to return.
     */
    fun getLatestTransaksi(limit: Int): Flow<List<Transaksi>>

    /**
     * Returns all transactions within a date range.
     *
     * @param start Inclusive start timestamp in milliseconds.
     * @param end Inclusive end timestamp in milliseconds.
     */
    fun getTransaksiBetween(start: Long, end: Long): Flow<List<Transaksi>>

    /**
     * Returns the total nominal for a given [tipe] within [start]..[end].
     *
     * @param tipe Transaction type string ("pemasukan" or "pengeluaran").
     * @param start Inclusive start timestamp in milliseconds.
     * @param end Inclusive end timestamp in milliseconds.
     */
    fun getTotalByTipeAndPeriod(tipe: String, start: Long, end: Long): Flow<Double?>

    /**
     * Returns the [Transaksi] with the given [id], or null if not found.
     *
     * @param id The unique identifier of the transaction.
     */
    suspend fun getTransaksiById(id: Int): Transaksi?

    /**
     * Inserts a new [Transaksi].
     *
     * @param transaksi The transaction to insert.
     */
    suspend fun insertTransaksi(transaksi: Transaksi)

    /**
     * Updates an existing [Transaksi].
     *
     * @param transaksi The transaction to update.
     */
    suspend fun updateTransaksi(transaksi: Transaksi)

    /**
     * Deletes a transaction by its [id].
     *
     * @param id The id of the transaction to delete.
     */
    suspend fun deleteTransaksiById(id: Int)
}

