package com.oratakashi.catatanuang.helpers

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val rupiahFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
private val displayDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.forLanguageTag("id-ID"))

/**
 * Formats a [Double] as Indonesian Rupiah currency string (e.g., "Rp10.000").
 */
fun Double.formatRupiah(): String = rupiahFormatter.format(this)

/**
 * Formats a [LocalDate] as a human-readable Indonesian date string (e.g., "22 Mar 2026").
 */
fun LocalDate.toDisplayString(): String = this.format(displayDateFormatter)

