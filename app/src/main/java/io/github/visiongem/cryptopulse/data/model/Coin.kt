package io.github.visiongem.cryptopulse.data.model

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val priceChangePercentage24h: Double,
    val marketCapRank: Int,
)

fun CoinDto.toDomain(): Coin = Coin(
    id = id,
    symbol = symbol.uppercase(),
    name = name,
    imageUrl = image,
    price = currentPrice ?: 0.0,
    priceChangePercentage24h = priceChangePercentage24h ?: 0.0,
    marketCapRank = marketCapRank ?: Int.MAX_VALUE,
)
