package com.oratakashi.catatanuang.navigation

/**
 * Sealed class defining all navigation destinations in the app.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property route Navigation route string.
 */
sealed class Screen(val route: String) {
    /** Home screen showing latest records and monthly summary. */
    data object Home : Screen("home")

    /** Record finance screen for adding a new transaction. */
    data object RecordFinance : Screen("record_finance")

    /** Reports screen with daily, weekly, and monthly tabs. */
    data object Report : Screen("report")

    /** Settings screen for language and theme preferences. */
    data object Settings : Screen("settings")

    /** Transaction detail screen showing full info for a single transaction. */
    data object TransaksiDetail : Screen("transaksi_detail/{transaksiId}") {
        /** Builds the navigation route for a specific transaction. */
        fun createRoute(transaksiId: Int) = "transaksi_detail/$transaksiId"
    }

    /** Edit transaction screen for modifying an existing transaction. */
    data object EditTransaksi : Screen("edit_transaksi/{transaksiId}") {
        /** Builds the navigation route for a specific transaction. */
        fun createRoute(transaksiId: Int) = "edit_transaksi/$transaksiId"
    }
}

