package com.oratakashi.catatanuang.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oratakashi.catatanuang.R
import com.oratakashi.catatanuang.domain.model.Transaksi
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import com.oratakashi.catatanuang.helpers.formatRupiah
import com.oratakashi.catatanuang.helpers.toDisplayString
import org.koin.androidx.compose.koinViewModel

/**
 * Transaction Detail screen showing full information for a single transaction.
 * Provides navigation to the edit screen and a delete action.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param transaksiId The ID of the transaction to display.
 * @param onNavigateBack Callback to navigate back.
 * @param onNavigateToEdit Callback to navigate to the edit screen for this transaction.
 * @param viewModel The [DetailViewModel] injected via Koin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    transaksiId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    viewModel: DetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(transaksiId) {
        viewModel.loadTransaksi(transaksiId)
    }

    LaunchedEffect(uiState) {
        if (uiState is DetailUiState.Deleted) {
            onNavigateBack()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.action_konfirmasi_hapus)) },
            text = { Text(stringResource(R.string.desc_konfirmasi_hapus)) },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteTransaksi(transaksiId)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.action_hapus))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.action_batal))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_detail_transaksi)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_kembali)
                        )
                    }
                },
                actions = {
                    if (uiState is DetailUiState.Success) {
                        IconButton(onClick = { onNavigateToEdit(transaksiId) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.action_edit)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is DetailUiState.Success -> {
                DetailContent(
                    transaksi = state.transaksi,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onDeleteClick = { showDeleteDialog = true }
                )
            }

            is DetailUiState.Deleted -> Unit
        }
    }
}

/**
 * Content layout for the Detail screen with a left-right row design per field.
 *
 * @param transaksi The [Transaksi] to display.
 * @param modifier Modifier for the root container.
 * @param onDeleteClick Callback when the delete button is tapped.
 */
@Composable
private fun DetailContent(
    transaksi: Transaksi,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    val nominalColor = if (transaksi.tipe == TransaksiTipe.PEMASUKAN) {
        Color(0xFF4CAF50)
    } else {
        Color(0xFFF44336)
    }
    val nominalPrefix = if (transaksi.tipe == TransaksiTipe.PEMASUKAN) "+" else "-"

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        DetailRow(
            label = stringResource(R.string.label_field_judul),
            value = transaksi.judul
        )
        HorizontalDivider()
        DetailRow(
            label = stringResource(R.string.label_field_tipe),
            value = if (transaksi.tipe == TransaksiTipe.PEMASUKAN)
                stringResource(R.string.label_pemasukan)
            else
                stringResource(R.string.label_pengeluaran),
            valueColor = nominalColor
        )
        HorizontalDivider()
        DetailRow(
            label = stringResource(R.string.label_field_nominal),
            value = "$nominalPrefix${transaksi.nominal.formatRupiah()}",
            valueColor = nominalColor,
            valueFontWeight = FontWeight.Bold
        )
        HorizontalDivider()
        DetailRow(
            label = stringResource(R.string.label_field_tanggal),
            value = transaksi.tanggal.toDisplayString()
        )
        HorizontalDivider()
        DetailRow(
            label = stringResource(R.string.label_field_kategori),
            value = transaksi.kategori
        )
        HorizontalDivider()
        DetailRow(
            label = stringResource(R.string.label_field_keterangan),
            value = transaksi.catatan.ifBlank { stringResource(R.string.label_tidak_ada_keterangan) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onDeleteClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.action_hapus))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * A single row in the detail layout showing a [label] on the left and a [value] on the right.
 *
 * @param label The caption/title shown on the left.
 * @param value The data value shown on the right.
 * @param valueColor Optional color override for the value text.
 * @param valueFontWeight Optional font weight override for the value text.
 */
@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified,
    valueFontWeight: FontWeight = FontWeight.Normal
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = valueFontWeight,
            color = if (valueColor == Color.Unspecified) MaterialTheme.colorScheme.onSurface else valueColor,
            modifier = Modifier
                .weight(0.6f)
                .padding(start = 8.dp)
        )
    }
}

