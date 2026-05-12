package io.github.visiongem.cryptopulse.util

import java.text.NumberFormat
import java.util.Locale

object PriceFormatter {

    fun formatUsd(price: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.maximumFractionDigits = when {
            price >= 1.0 -> 2
            price >= 0.01 -> 4
            else -> 8
        }
        formatter.minimumFractionDigits = 2
        return formatter.format(price)
    }

    fun formatPercent(percent: Double): String {
        val sign = if (percent >= 0) "+" else ""
        return "%s%.2f%%".format(sign, percent)
    }
}
