package io.github.visiongem.cryptopulse.feature.detail

import androidx.annotation.StringRes
import io.github.visiongem.cryptopulse.R

enum class ChartPeriod(val days: Int, @StringRes val labelRes: Int) {
    DAY(1, R.string.chart_period_1d),
    WEEK(7, R.string.chart_period_7d),
    MONTH(30, R.string.chart_period_30d),
    YEAR(365, R.string.chart_period_1y),
}
