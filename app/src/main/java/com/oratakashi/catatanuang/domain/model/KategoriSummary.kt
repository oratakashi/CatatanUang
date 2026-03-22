package com.oratakashi.catatanuang.domain.model

/**
 * Summary of transactions grouped by a single category for the pie chart accordion.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property kategori The category name.
 * @property tipe The transaction type (income or expense).
 * @property total Total nominal for this category in the selected period.
 * @property transaksiList Individual transactions belonging to this category.
 */
data class KategoriSummary(
    val kategori: String,
    val tipe: TransaksiTipe,
    val total: Double,
    val transaksiList: List<Transaksi>
)

