package com.oratakashi.catatanuang.domain.model

import java.time.LocalDate

/**
 * Domain model representing a single financial transaction.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property id Unique identifier of the transaction.
 * @property judul Title or short label of the transaction.
 * @property nominal Amount of the transaction.
 * @property kategori Category name (e.g., "Makanan", "Transportasi").
 * @property tipe Whether this is income or expense.
 * @property catatan Optional notes or description.
 * @property tanggal Date of the transaction.
 */
data class Transaksi(
    val id: Int = 0,
    val judul: String,
    val nominal: Double,
    val kategori: String,
    val tipe: TransaksiTipe,
    val catatan: String = "",
    val tanggal: LocalDate
)

