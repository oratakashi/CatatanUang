package com.oratakashi.catatanuang.domain.model

/**
 * Represents the available app language options.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 * @property code BCP 47 language tag used to create the locale (e.g. "id", "en").
 */
enum class AppLanguage(val code: String) {
    /** Bahasa Indonesia. */
    INDONESIAN("id"),

    /** English. */
    ENGLISH("en")
}

