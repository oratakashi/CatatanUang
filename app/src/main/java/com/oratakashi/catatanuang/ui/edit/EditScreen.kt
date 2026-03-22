package com.oratakashi.catatanuang.ui.edit

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oratakashi.catatanuang.R
import com.oratakashi.catatanuang.domain.model.TransaksiTipe
import com.oratakashi.catatanuang.helpers.ThousandSeparatorTransformation
import com.oratakashi.catatanuang.helpers.digitsOnly
import com.oratakashi.catatanuang.helpers.toDisplayString
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/** Predefined transaction categories — must stay in sync with RecordScreen. */
private val KATEGORI_LIST = listOf(
    "Makanan & Minuman",
    "Transportasi",
    "Belanja",
    "Hiburan",
    "Kesehatan",
    "Pendidikan",
    "Tagihan & Utilitas",
    "Gaji",
    "Investasi",
    "Lainnya"
)

/**
 * Edit Transaction screen composable for modifying an existing financial transaction.
 *
 * The form is pre-filled with the existing data loaded by [EditViewModel].
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @param transaksiId The ID of the transaction to edit.
 * @param onNavigateBack Callback to navigate back.
 * @param viewModel The [EditViewModel] injected via Koin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    transaksiId: Int,
    onNavigateBack: () -> Unit,
    viewModel: EditViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val existingTransaksi by viewModel.existingTransaksi.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var isFormInitialized by remember { mutableStateOf(false) }
    var judul by rememberSaveable { mutableStateOf("") }
    var nominalText by rememberSaveable { mutableStateOf("") }
    var kategori by rememberSaveable { mutableStateOf(KATEGORI_LIST.first()) }
    var catatan by rememberSaveable { mutableStateOf("") }
    var tipe by rememberSaveable { mutableStateOf(TransaksiTipe.PENGELUARAN) }
    var tanggal by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var kategoriExpanded by remember { mutableStateOf(false) }

    // Load the transaction once when the screen is first opened.
    LaunchedEffect(transaksiId) {
        viewModel.loadTransaksi(transaksiId)
    }

    // Pre-fill the form as soon as the transaction data is available.
    LaunchedEffect(existingTransaksi) {
        val data = existingTransaksi
        if (data != null && !isFormInitialized) {
            judul = data.judul
            nominalText = data.nominal.toLong().toString()
            kategori = data.kategori
            catatan = data.catatan
            tipe = data.tipe
            tanggal = data.tanggal
            isFormInitialized = true
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is EditUiState.Success -> onNavigateBack()
            is EditUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    val judulError = judul.isBlank() && uiState is EditUiState.Error
    val nominalError = (nominalText.digitsOnly().toLongOrNull() ?: 0L) <= 0L && uiState is EditUiState.Error

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = tanggal
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        tanggal = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.action_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.action_batal))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_edit_transaksi)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_kembali)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        // Show a full-screen loader only while the form hasn't been initialized yet.
        if (!isFormInitialized && uiState is EditUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Tipe toggle (Pemasukan / Pengeluaran)
            Text(
                text = stringResource(R.string.label_tipe_transaksi),
                style = MaterialTheme.typography.bodyMedium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = tipe == TransaksiTipe.PEMASUKAN,
                    onClick = { tipe = TransaksiTipe.PEMASUKAN },
                    label = { Text(stringResource(R.string.label_pemasukan)) }
                )
                FilterChip(
                    selected = tipe == TransaksiTipe.PENGELUARAN,
                    onClick = { tipe = TransaksiTipe.PENGELUARAN },
                    label = { Text(stringResource(R.string.label_pengeluaran)) }
                )
            }

            // Judul
            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text(stringResource(R.string.hint_judul)) },
                modifier = Modifier.fillMaxWidth(),
                isError = judulError,
                singleLine = true
            )

            // Nominal
            OutlinedTextField(
                value = nominalText,
                onValueChange = { input -> nominalText = input.digitsOnly() },
                label = { Text(stringResource(R.string.hint_nominal)) },
                modifier = Modifier.fillMaxWidth(),
                isError = nominalError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                prefix = { Text("Rp ") },
                visualTransformation = ThousandSeparatorTransformation()
            )

            // Tanggal
            OutlinedTextField(
                value = tanggal.toDisplayString(),
                onValueChange = {},
                label = { Text(stringResource(R.string.hint_tanggal)) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = stringResource(R.string.hint_tanggal)
                        )
                    }
                }
            )

            // Kategori dropdown
            ExposedDropdownMenuBox(
                expanded = kategoriExpanded,
                onExpandedChange = { kategoriExpanded = it }
            ) {
                OutlinedTextField(
                    value = kategori,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.hint_kategori)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = kategoriExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = kategoriExpanded,
                    onDismissRequest = { kategoriExpanded = false }
                ) {
                    KATEGORI_LIST.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                kategori = item
                                kategoriExpanded = false
                            }
                        )
                    }
                }
            }

            // Keterangan
            OutlinedTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = { Text(stringResource(R.string.hint_keterangan)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Save button
            Button(
                onClick = {
                    viewModel.updateTransaksi(
                        id = transaksiId,
                        judul = judul,
                        nominal = nominalText.digitsOnly().toDoubleOrNull() ?: 0.0,
                        kategori = kategori,
                        catatan = catatan,
                        tipe = tipe,
                        tanggal = tanggal
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is EditUiState.Loading
            ) {
                if (uiState is EditUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.action_simpan))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

