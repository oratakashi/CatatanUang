package com.oratakashi.catatanuang.data.local.dao

import androidx.room.*
import com.oratakashi.catatanuang.data.local.entity.TransaksiEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the [TransaksiEntity] table.
 * Provides all CRUD operations and query methods for financial records.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
@Dao
interface TransaksiDao {

    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC")
    fun getAllTransaksi(): Flow<List<TransaksiEntity>>

    @Query("SELECT * FROM transaksi WHERE tipe = :tipe ORDER BY tanggal DESC")
    fun getTransaksiByTipe(tipe: String): Flow<List<TransaksiEntity>>

    @Query("SELECT * FROM transaksi WHERE id = :id")
    suspend fun getTransaksiById(id: Int): TransaksiEntity?

    @Query("SELECT SUM(nominal) FROM transaksi WHERE tipe = 'pemasukan'")
    fun getTotalPemasukan(): Flow<Double?>

    @Query("SELECT SUM(nominal) FROM transaksi WHERE tipe = 'pengeluaran'")
    fun getTotalPengeluaran(): Flow<Double?>

    /**
     * Returns the [limit] most recent transactions ordered by date descending.
     *
     * @param limit Maximum number of records to return.
     */
    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC LIMIT :limit")
    fun getLatestTransaksi(limit: Int): Flow<List<TransaksiEntity>>

    /**
     * Returns all transactions whose [TransaksiEntity.tanggal] falls within [start]..[end] (epoch millis).
     *
     * @param start Inclusive start timestamp in milliseconds.
     * @param end Inclusive end timestamp in milliseconds.
     */
    @Query("SELECT * FROM transaksi WHERE tanggal >= :start AND tanggal <= :end ORDER BY tanggal DESC")
    fun getTransaksiBetween(start: Long, end: Long): Flow<List<TransaksiEntity>>

    /**
     * Returns the total nominal for a given [tipe] within [start]..[end] (epoch millis).
     *
     * @param tipe Transaction type string, e.g. "pemasukan" or "pengeluaran".
     * @param start Inclusive start timestamp in milliseconds.
     * @param end Inclusive end timestamp in milliseconds.
     */
    @Query("SELECT SUM(nominal) FROM transaksi WHERE tipe = :tipe AND tanggal >= :start AND tanggal <= :end")
    fun getTotalByTipeAndPeriod(tipe: String, start: Long, end: Long): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksi: TransaksiEntity)

    @Update
    suspend fun updateTransaksi(transaksi: TransaksiEntity)

    @Delete
    suspend fun deleteTransaksi(transaksi: TransaksiEntity)

    @Query("DELETE FROM transaksi WHERE id = :id")
    suspend fun deleteTransaksiById(id: Int)
}



