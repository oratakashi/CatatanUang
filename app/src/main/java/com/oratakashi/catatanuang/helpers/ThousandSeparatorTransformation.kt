package com.oratakashi.catatanuang.helpers

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A [VisualTransformation] that formats a raw digit string with thousand-separator dots.
 * For example, "1000000" is displayed as "1.000.000" while the underlying value stays numeric.
 *
 * Only digits are accepted; any non-digit character is stripped before formatting.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
class ThousandSeparatorTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        val formatted = formatWithDots(original)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Count how many dots are inserted before this offset in the original string.
                val dotsBeforeOffset = dotCountBefore(original, offset)
                return (offset + dotsBeforeOffset).coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Walk the formatted string and count non-dot chars up to the given offset.
                var originalCount = 0
                for (i in 0 until offset.coerceAtMost(formatted.length)) {
                    if (formatted[i] != '.') originalCount++
                }
                return originalCount.coerceAtMost(original.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }

    companion object {
        /**
         * Inserts dot separators every 3 digits from the right.
         *
         * @param input Raw digit string.
         * @return Formatted string with dot separators.
         */
        fun formatWithDots(input: String): String {
            if (input.isEmpty()) return input
            val reversed = input.reversed()
            return reversed
                .chunked(3)
                .joinToString(".")
                .reversed()
        }

        /**
         * Counts how many dots would appear before [originalOffset] in the formatted string.
         *
         * @param original The raw digit string.
         * @param originalOffset The cursor position in the raw string.
         */
        private fun dotCountBefore(original: String, originalOffset: Int): Int {
            val totalLength = original.length
            if (totalLength == 0) return 0
            // Dots are inserted every 3 digits from the right.
            // Number of dots in full formatted string = (totalLength - 1) / 3
            // Digits to the right of offset = totalLength - originalOffset
            val digitsRight = totalLength - originalOffset
            val totalDots = (totalLength - 1) / 3
            val dotsRight = if (digitsRight == 0) 0 else (digitsRight - 1) / 3
            return totalDots - dotsRight
        }
    }
}

/**
 * Strips all non-digit characters from a string, keeping only numeric input safe for parsing.
 *
 * @return A string containing only digit characters.
 */
fun String.digitsOnly(): String = filter { it.isDigit() }

