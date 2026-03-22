package com.oratakashi.catatanuang.domain.mapper

import com.oratakashi.catatanuang.data.local.entity.TransaksiEntity
import com.oratakashi.catatanuang.domain.model.Transaksi
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Extension functions to map between [TransaksiEntity] (data layer) and [Transaksi] (domain layer).
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */

/**
 * Converts a [TransaksiEntity] to a domain [Transaksi].
 */
fun TransaksiEntity.toDomain(): Transaksi = Transaksi(
    id = id,
    judul = judul,
    nominal = nominal,
    kategori = kategori,
    tipe = TransaksiTipe.fromValue(tipe),
    catatan = catatan,
    tanggal = Instant.ofEpochMilli(tanggal)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
)

/**
 * Converts a domain [Transaksi] to a [TransaksiEntity] for storage.
 */
fun Transaksi.toEntity(): TransaksiEntity = TransaksiEntity(
    id = id,
    judul = judul,
    nominal = nominal,
    kategori = kategori,
    tipe = tipe.value,
    catatan = catatan,
    tanggal = tanggal
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
)

