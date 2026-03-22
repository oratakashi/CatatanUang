package com.oratakashi.catatanuang.domain.usecase

import com.oratakashi.catatanuang.domain.model.LaporanSummary
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import com.oratakashi.catatanuang.domain.repository.ITransaksiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * Use case for retrieving a weekly financial report.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property repository The transaction repository.
 */
class GetLaporanMingguanUseCase(private val repository: ITransaksiRepository) {

    /**
     * Returns a [Flow] of [LaporanSummary] for the week containing [date].
     * Week starts on Monday.
     *
     * @param date Any date within the target week. Defaults to today.
     */
    operator fun invoke(date: LocalDate = LocalDate.now()): Flow<LaporanSummary> {
        val zone = ZoneId.systemDefault()
        val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = startOfWeek.plusDays(6)

        val start = startOfWeek.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = endOfWeek.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        val transaksiFlow = repository.getTransaksiBetween(start, end)
        val pemasukanFlow = repository.getTotalByTipeAndPeriod(TransaksiTipe.PEMASUKAN.value, start, end)
        val pengeluaranFlow = repository.getTotalByTipeAndPeriod(TransaksiTipe.PENGELUARAN.value, start, end)

        return combine(transaksiFlow, pemasukanFlow, pengeluaranFlow) { list, pemasukan, pengeluaran ->
            val totalPemasukan = pemasukan ?: 0.0
            val totalPengeluaran = pengeluaran ?: 0.0
            LaporanSummary(
                totalPemasukan = totalPemasukan,
                totalPengeluaran = totalPengeluaran,
                saldo = totalPemasukan - totalPengeluaran,
                transaksiList = list
            )
        }
    }
}

