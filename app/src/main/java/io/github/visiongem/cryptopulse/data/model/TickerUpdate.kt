package io.github.visiongem.cryptopulse.data.model

import io.github.visiongem.cryptopulse.data.network.BinanceTickerDto

data class TickerUpdate(
    val symbol: String,
    val price: Double,
)

fun BinanceTickerDto.toDomain(): TickerUpdate? {
    if (!symbol.endsWith(USDT_SUFFIX)) return null
    val price = closePrice.toDoubleOrNull() ?: return null
    return TickerUpdate(
        symbol = symbol.removeSuffix(USDT_SUFFIX),
        price = price,
    )
}

private const val USDT_SUFFIX = "USDT"
