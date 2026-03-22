package com.oratakashi.catatanuang.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.futured.donut.compose.DonutProgress
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import com.oratakashi.catatanuang.R
import com.oratakashi.catatanuang.domain.model.KategoriSummary
import com.oratakashi.catatanuang.domain.model.PieChartPeriod
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import com.oratakashi.catatanuang.helpers.formatRupiah
import com.oratakashi.catatanuang.helpers.toDisplayString
import com.oratakashi.catatanuang.ui.home.PieChartUiState

/** Palette of distinct colors for kategori slices in the pie chart. */
private val KATEGORI_COLORS = listOf(
    Color(0xFF4CAF50), Color(0xFFF44336), Color(0xFF2196F3), Color(0xFFFF9800),
    Color(0xFF9C27B0), Color(0xFF00BCD4), Color(0xFFE91E63), Color(0xFF8BC34A),
    Color(0xFFFF5722), Color(0xFF607D8B)
)

/**
 * Displays a pie chart with period filter chips (Harian / Mingguan / Bulanan),
 * income vs expense legend, and an accordion breakdown per category.
 *
 * Accepts a [PieChartUiState] so that only this composable recomposes when the
 * period changes — the rest of the Home screen is unaffected.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param chartState The current [PieChartUiState] driven by the selected period.
 * @param onPeriodSelected Callback when the user selects a different period.
 * @param modifier A [Modifier] for styling.
 */
@Composable
fun MonthlyPieChart(
    chartState: PieChartUiState,
    onPeriodSelected: (PieChartPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    // Derive display values — fall back to zeros/empty while loading so layout is stable
    val totalPemasukan = if (chartState is PieChartUiState.Success) chartState.totalPemasukan else 0.0
    val totalPengeluaran = if (chartState is PieChartUiState.Success) chartState.totalPengeluaran else 0.0
    val kategoriList = if (chartState is PieChartUiState.Success) chartState.kategoriList else emptyList()
    val selectedPeriod = if (chartState is PieChartUiState.Success) chartState.selectedPeriod else PieChartPeriod.BULANAN

    val total = totalPemasukan + totalPengeluaran
    val colorPemasukan = Color(0xFF4CAF50)
    val colorPengeluaran = Color(0xFFF44336)
    val colorEmpty = Color(0xFFE0E0E0)

    // Build DonutSection list per kategori; use KATEGORI_COLORS cycled by index.
    // The Donut library requires a fixed section count — use amount=0f for empty sections.
    val donutSections: List<DonutSection> = remember(kategoriList) {
        if (kategoriList.isEmpty()) {
            listOf(DonutSection(amount = 0f, color = colorEmpty))
        } else {
            kategoriList.mapIndexed { index, summary ->
                DonutSection(
                    amount = summary.total.toFloat(),
                    color = KATEGORI_COLORS[index % KATEGORI_COLORS.size]
                )
            }
        }
    }

    // Parallel color list used by the accordion (same order as donutSections).
    val sliceColors: List<Color> = remember(kategoriList) {
        kategoriList.mapIndexed { index, _ ->
            KATEGORI_COLORS[index % KATEGORI_COLORS.size]
        }
    }

    var accordionExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title
            Text(
                text = stringResource(R.string.label_ringkasan_bulanan),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Period selector chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PieChartPeriod.entries.forEach { period ->
                    FilterChip(
                        selected = selectedPeriod == period,
                        onClick = { onPeriodSelected(period) },
                        label = {
                            Text(
                                text = when (period) {
                                    PieChartPeriod.HARIAN -> stringResource(R.string.label_harian)
                                    PieChartPeriod.MINGGUAN -> stringResource(R.string.label_mingguan)
                                    PieChartPeriod.BULANAN -> stringResource(R.string.label_bulanan)
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Donut chart — shows a spinner while data is loading
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (chartState is PieChartUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(160.dp).padding(56.dp))
                } else {
                    DonutProgress(
                        model = DonutModel(
                            cap = if (total > 0.0) total.toFloat() else 1f,
                            masterProgress = 1f,
                            gapWidthDegrees = 25f,
                            gapAngleDegrees = 270f,
                            strokeWidth = 40f,
                            backgroundLineColor = colorEmpty,
                            sections = donutSections
                        ),
                        modifier = Modifier.size(160.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pemasukan / Pengeluaran legend summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem(
                    color = colorPemasukan,
                    label = stringResource(R.string.label_pemasukan),
                    value = totalPemasukan.formatRupiah()
                )
                LegendItem(
                    color = colorPengeluaran,
                    label = stringResource(R.string.label_pengeluaran),
                    value = totalPengeluaran.formatRupiah()
                )
            }

            // Accordion: Detail per kategori
            if (kategoriList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { accordionExpanded = !accordionExpanded }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.label_detail_kategori),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = if (accordionExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }

                AnimatedVisibility(
                    visible = accordionExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        kategoriList.forEachIndexed { index, summary ->
                            KategoriAccordionItem(
                                summary = summary,
                                color = sliceColors[index]
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * A single expandable accordion row showing one category's total and its transaction list.
 *
 * @param summary The [KategoriSummary] to display.
 * @param color The color assigned to this kategori slice.
 */
@Composable
private fun KategoriAccordionItem(
    summary: KategoriSummary,
    color: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val nominalColor = if (summary.tipe == TransaksiTipe.PEMASUKAN) Color(0xFF4CAF50) else Color(0xFFF44336)
    val prefix = if (summary.tipe == TransaksiTipe.PEMASUKAN) "+" else "-"

    Column {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Canvas(modifier = Modifier.size(10.dp)) { drawCircle(color = color) }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = summary.kategori,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${summary.transaksiList.size} transaksi",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$prefix${summary.total.formatRupiah()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = nominalColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Expandable transaction list for this kategori
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier.padding(start = 18.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                summary.transaksiList.forEach { transaksi ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = transaksi.judul,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = transaksi.tanggal.toDisplayString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$prefix${transaksi.nominal.formatRupiah()}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = nominalColor
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

/**
 * A single legend row showing a colored dot, label, and value.
 *
 * @param color The legend dot color.
 * @param label The legend label.
 * @param value The formatted value string.
 */
@Composable
private fun LegendItem(
    color: Color,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(12.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall)
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

