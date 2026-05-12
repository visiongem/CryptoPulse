package io.github.visiongem.cryptopulse.data.model

import io.github.visiongem.cryptopulse.data.network.BinanceTickerDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TickerUpdateTest {

    @Test
    fun `maps BTCUSDT ticker to BTC TickerUpdate`() {
        val dto = sampleDto(symbol = "BTCUSDT", price = "102345.67")
        val result = dto.toDomain()
        assertThat(result).isEqualTo(TickerUpdate(symbol = "BTC", price = 102345.67))
    }

    @Test
    fun `returns null for non-USDT pairs`() {
        val dto = sampleDto(symbol = "BTCBUSD", price = "100.0")
        assertThat(dto.toDomain()).isNull()
    }

    @Test
    fun `returns null when price cannot be parsed`() {
        val dto = sampleDto(symbol = "BTCUSDT", price = "not-a-number")
        assertThat(dto.toDomain()).isNull()
    }

    private fun sampleDto(symbol: String, price: String) = BinanceTickerDto(
        eventType = "24hrMiniTicker",
        eventTime = 0L,
        symbol = symbol,
        closePrice = price,
        openPrice = "0",
        highPrice = "0",
        lowPrice = "0",
        baseVolume = "0",
        quoteVolume = "0",
    )
}
