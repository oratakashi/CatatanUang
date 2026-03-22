package com.oratakashi.catatanuang.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.oratakashi.catatanuang.domain.model.Transaksi
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import com.oratakashi.catatanuang.helpers.formatRupiah
import com.oratakashi.catatanuang.helpers.toDisplayString

/**
 * A single transaction list item card used on the Home and Report screens.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param transaksi The [Transaksi] domain model to display.
 * @param modifier A [Modifier] for styling.
 * @param onClick Optional callback invoked when the item is tapped.
 */
@Composable
fun TransaksiItem(
    transaksi: Transaksi,
    modifier: Modifier = Modifier,
    onClick: ((Transaksi) -> Unit)? = null
) {
    val nominalColor = if (transaksi.tipe == TransaksiTipe.PEMASUKAN) {
        Color(0xFF4CAF50)
    } else {
        Color(0xFFF44336)
    }
    val prefix = if (transaksi.tipe == TransaksiTipe.PEMASUKAN) "+" else "-"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable { onClick(transaksi) }
                else Modifier
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaksi.judul.replaceFirstChar { it.uppercaseChar() },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = transaksi.kategori,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = transaksi.tanggal.toDisplayString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "$prefix${transaksi.nominal.formatRupiah()}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = nominalColor
            )
        }
    }
}
