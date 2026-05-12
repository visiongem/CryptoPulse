package io.github.visiongem.cryptopulse.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {

    fun forChartPoint(timestampMs: Long, days: Int): String {
        val pattern = when {
            days <= 1 -> "HH:mm"
            days <= 7 -> "EEE HH:mm"
            days <= 30 -> "MMM d"
            else -> "MMM yyyy"
        }
        return Instant.ofEpochMilli(timestampMs)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
    }
}
