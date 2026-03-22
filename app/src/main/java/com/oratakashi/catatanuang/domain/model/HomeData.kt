package com.oratakashi.catatanuang.domain.model

/**
 * Aggregated data for the Home screen.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property latestTransaksi The 6 most recent transactions.
 * @property totalPemasukan Total income for the selected period.
 * @property totalPengeluaran Total expense for the selected period.
 * @property saldo Net balance for the current month (always monthly).
 * @property kategoriList Breakdown of transactions grouped by category for the selected period.
 * @property activePeriod The currently selected pie chart period.
 */
data class HomeData(
    val latestTransaksi: List<Transaksi>,
    val totalPemasukan: Double,
    val totalPengeluaran: Double,
    val saldo: Double,
    val kategoriList: List<KategoriSummary>,
    val activePeriod: PieChartPeriod
)



