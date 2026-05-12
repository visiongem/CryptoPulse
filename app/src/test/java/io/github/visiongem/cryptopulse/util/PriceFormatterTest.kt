package io.github.visiongem.cryptopulse.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PriceFormatterTest {

    @Test
    fun `formatUsd uses two decimals for prices >= 1`() {
        assertThat(PriceFormatter.formatUsd(102345.678)).isEqualTo("$102,345.68")
    }

    @Test
    fun `formatUsd uses four decimals for prices in [0_01, 1)`() {
        assertThat(PriceFormatter.formatUsd(0.1234)).isEqualTo("$0.1234")
    }

    @Test
    fun `formatUsd uses eight decimals for very small prices`() {
        assertThat(PriceFormatter.formatUsd(0.00001234)).isEqualTo("$0.00001234")
    }

    @Test
    fun `formatPercent prefixes plus sign for non-negative`() {
        assertThat(PriceFormatter.formatPercent(2.345)).isEqualTo("+2.35%")
        assertThat(PriceFormatter.formatPercent(0.0)).isEqualTo("+0.00%")
    }

    @Test
    fun `formatPercent keeps minus sign for negative`() {
        assertThat(PriceFormatter.formatPercent(-1.789)).isEqualTo("-1.79%")
    }
}
