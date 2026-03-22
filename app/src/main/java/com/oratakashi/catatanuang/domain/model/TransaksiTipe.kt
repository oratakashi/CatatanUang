package com.oratakashi.catatanuang.domain.model

/**
 * Represents the type of a financial transaction.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
enum class TransaksiTipe(val value: String) {
    /** Income transaction. */
    PEMASUKAN("pemasukan"),

    /** Expense transaction. */
    PENGELUARAN("pengeluaran");

    companion object {
        /**
         * Converts a raw string value to a [TransaksiTipe], defaulting to [PENGELUARAN].
         *
         * @param value The raw string from database.
         */
        fun fromValue(value: String): TransaksiTipe =
            entries.firstOrNull { it.value == value } ?: PENGELUARAN
    }
}

