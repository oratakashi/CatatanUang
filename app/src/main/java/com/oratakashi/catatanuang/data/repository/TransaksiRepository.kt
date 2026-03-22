package com.oratakashi.catatanuang.data.repository

import com.oratakashi.catatanuang.data.local.dao.TransaksiDao
import com.oratakashi.catatanuang.domain.mapper.toDomain
import com.oratakashi.catatanuang.domain.mapper.toEntity
import com.oratakashi.catatanuang.domain.model.Transaksi
import com.oratakashi.catatanuang.domain.repository.ITransaksiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [ITransaksiRepository] backed by a local Room database.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property dao The Room DAO for transaksi operations.
 */
class TransaksiRepository(private val dao: TransaksiDao) : ITransaksiRepository {

    override fun getAllTransaksi(): Flow<List<Transaksi>> =
        dao.getAllTransaksi().map { list -> list.map { it.toDomain() } }

    override fun getLatestTransaksi(limit: Int): Flow<List<Transaksi>> =
        dao.getLatestTransaksi(limit).map { list -> list.map { it.toDomain() } }

    override fun getTransaksiBetween(start: Long, end: Long): Flow<List<Transaksi>> =
        dao.getTransaksiBetween(start, end).map { list -> list.map { it.toDomain() } }

    override fun getTotalByTipeAndPeriod(tipe: String, start: Long, end: Long): Flow<Double?> =
        dao.getTotalByTipeAndPeriod(tipe, start, end)

    override suspend fun getTransaksiById(id: Int): Transaksi? =
        dao.getTransaksiById(id)?.toDomain()

    override suspend fun insertTransaksi(transaksi: Transaksi) =
        dao.insertTransaksi(transaksi.toEntity())

    override suspend fun updateTransaksi(transaksi: Transaksi) =
        dao.updateTransaksi(transaksi.toEntity())

    override suspend fun deleteTransaksiById(id: Int) =
        dao.deleteTransaksiById(id)
}

