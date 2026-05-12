package io.github.visiongem.cryptopulse.data.model

data class ChartPoint(
    val timestamp: Long,
    val price: Double,
)

data class ChartData(
    val coinId: String,
    val days: Int,
    val points: List<ChartPoint>,
) {
    val minPrice: Double get() = points.minOfOrNull { it.price } ?: 0.0
    val maxPrice: Double get() = points.maxOfOrNull { it.price } ?: 0.0
    val isPositive: Boolean
        get() {
            val first = points.firstOrNull()?.price ?: return true
            val last = points.lastOrNull()?.price ?: return true
            return last >= first
        }
}

fun MarketChartDto.toDomain(coinId: String, days: Int): ChartData = ChartData(
    coinId = coinId,
    days = days,
    points = prices.mapNotNull { entry ->
        if (entry.size < 2) return@mapNotNull null
        ChartPoint(timestamp = entry[0].toLong(), price = entry[1])
    },
)
