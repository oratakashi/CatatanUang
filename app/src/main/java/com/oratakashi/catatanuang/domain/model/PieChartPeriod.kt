package com.oratakashi.catatanuang.domain.model

/**
 * Represents the period filter options for the pie chart summary on the Home screen.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
enum class PieChartPeriod {
    /** Summary for today only. */
    HARIAN,

    /** Summary for the current week (Mon–Sun). */
    MINGGUAN,

    /** Summary for the current month. */
    BULANAN
}

