package com.oratakashi.catatanuang.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oratakashi.catatanuang.R
import com.oratakashi.catatanuang.helpers.formatRupiah

/**
 * Displays the current month's net balance card on the Home screen.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param saldo The current month net balance.
 * @param modifier A [Modifier] for styling.
 */
@Composable
fun BalanceCard(
    saldo: Double,
    modifier: Modifier = Modifier
) {
    val balanceColor = if (saldo >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.label_saldo_bulan_ini),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = saldo.formatRupiah(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = balanceColor
            )
        }
    }
}

