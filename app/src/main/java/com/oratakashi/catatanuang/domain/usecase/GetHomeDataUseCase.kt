package com.oratakashi.catatanuang.domain.usecase

import com.oratakashi.catatanuang.domain.model.HomeData
import com.oratakashi.catatanuang.domain.model.KategoriSummary
import com.oratakashi.catatanuang.domain.model.PieChartPeriod
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import com.oratakashi.catatanuang.domain.repository.ITransaksiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * Use case for retrieving data needed by the Home screen.
 * Returns the 6 latest transactions, period income/expense summary, and per-category breakdown.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property repository The transaction repository.
 */
class GetHomeDataUseCase(private val repository: ITransaksiRepository) {

    private companion object {
        /** Number of latest transactions to display on the home screen. */
        const val LATEST_TRANSAKSI_LIMIT = 6
    }

    /**
     * Returns a [Flow] of [HomeData] for the given [period].
     *
     * @param period The pie chart period to summarize. Defaults to [PieChartPeriod.BULANAN].
     */
    operator fun invoke(period: PieChartPeriod = PieChartPeriod.BULANAN): Flow<HomeData> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()

        // Period range for pie chart
        val (periodStart, periodEnd) = resolvePeriodRange(today, period, zone)

        // Monthly range always used for saldo card
        val yearMonth = YearMonth.from(today)
        val monthStart = yearMonth.atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val monthEnd = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        val latestFlow = repository.getLatestTransaksi(LATEST_TRANSAKSI_LIMIT)
        val periodTransaksiFlow = repository.getTransaksiBetween(periodStart, periodEnd)
        val pemasukanFlow = repository.getTotalByTipeAndPeriod(TransaksiTipe.PEMASUKAN.value, periodStart, periodEnd)
        val pengeluaranFlow = repository.getTotalByTipeAndPeriod(TransaksiTipe.PENGELUARAN.value, periodStart, periodEnd)
        val saldoPemasukanFlow = repository.getTotalByTipeAndPeriod(TransaksiTipe.PEMASUKAN.value, monthStart, monthEnd)
        val saldoPengeluaranFlow = repository.getTotalByTipeAndPeriod(TransaksiTipe.PENGELUARAN.value, monthStart, monthEnd)

        // Chain combines because Kotlin's combine() supports max 5 flows at a time
        val periodSummaryFlow = combine(periodTransaksiFlow, pemasukanFlow, pengeluaranFlow) {
            periodList, pemasukan, pengeluaran ->
            Triple(periodList, pemasukan ?: 0.0, pengeluaran ?: 0.0)
        }
        val saldoFlow = combine(saldoPemasukanFlow, saldoPengeluaranFlow) { sp, se ->
            (sp ?: 0.0) - (se ?: 0.0)
        }

        return combine(latestFlow, periodSummaryFlow, saldoFlow) { latest, periodSummary, saldo ->
            val (periodList, totalPemasukan, totalPengeluaran) = periodSummary

            val kategoriList: List<KategoriSummary> = periodList
                .groupBy { transaksi -> Pair(transaksi.kategori, transaksi.tipe) }
                .map { entry ->
                    val (kategori, tipe) = entry.key
                    KategoriSummary(
                        kategori = kategori,
                        tipe = tipe,
                        total = entry.value.sumOf { it.nominal },
                        transaksiList = entry.value
                    )
                }
                .sortedByDescending { it.total }

            HomeData(
                latestTransaksi = latest,
                totalPemasukan = totalPemasukan,
                totalPengeluaran = totalPengeluaran,
                saldo = saldo,
                kategoriList = kategoriList,
                activePeriod = period
            )
        }
    }

    /**
     * Resolves start and end epoch millis for a given [period] relative to [today].
     *
     * @param today The reference date.
     * @param period The selected [PieChartPeriod].
     * @param zone The time zone.
     * @return A [Pair] of start and end epoch millis (inclusive).
     */
    private fun resolvePeriodRange(
        today: LocalDate,
        period: PieChartPeriod,
        zone: ZoneId
    ): Pair<Long, Long> = when (period) {
        PieChartPeriod.HARIAN -> {
            val start = today.atStartOfDay(zone).toInstant().toEpochMilli()
            val end = today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
            start to end
        }
        PieChartPeriod.MINGGUAN -> {
            val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val start = startOfWeek.atStartOfDay(zone).toInstant().toEpochMilli()
            val end = startOfWeek.plusDays(7).atStartOfDay(zone).toInstant().toEpochMilli() - 1
            start to end
        }
        PieChartPeriod.BULANAN -> {
            val yearMonth = YearMonth.from(today)
            val start = yearMonth.atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
            val end = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
            start to end
        }
    }
}

