package com.oratakashi.catatanuang.domain.model

/**
 * Summary of financial transactions for a specific period.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property totalPemasukan Total income amount in the period.
 * @property totalPengeluaran Total expense amount in the period.
 * @property saldo Net balance (pemasukan - pengeluaran).
 * @property transaksiList List of individual transactions in the period.
 */
data class LaporanSummary(
    val totalPemasukan: Double,
    val totalPengeluaran: Double,
    val saldo: Double,
    val transaksiList: List<Transaksi>
)

