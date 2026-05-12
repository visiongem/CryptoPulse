package io.github.visiongem.cryptopulse.feature.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.visiongem.cryptopulse.data.model.ChartPoint
import io.github.visiongem.cryptopulse.data.model.Coin
import io.github.visiongem.cryptopulse.ui.theme.SignalNegative
import io.github.visiongem.cryptopulse.ui.theme.SignalPositive
import io.github.visiongem.cryptopulse.util.DateFormatter
import io.github.visiongem.cryptopulse.util.PriceFormatter

private val FAVORITE_AMBER = Color(0xFFFBBF24)

@Composable
fun CoinDetailHeader(
    coin: Coin,
    hoveredPoint: ChartPoint?,
    chartDays: Int,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayedPrice = hoveredPoint?.price ?: coin.price
    val displayedSecondary = hoveredPoint?.let {
        DateFormatter.forChartPoint(it.timestamp, chartDays)
    } ?: PriceFormatter.formatPercent(coin.priceChangePercentage24h)
    val secondaryColor = when {
        hoveredPoint != null -> MaterialTheme.colorScheme.onSurfaceVariant
        coin.priceChangePercentage24h >= 0 -> SignalPositive
        else -> SignalNegative
    }

    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = coin.imageUrl,
                contentDescription = coin.symbol,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = coin.symbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (coin.isFavorite) {
                        Icons.Filled.Star
                    } else {
                        Icons.Outlined.StarOutline
                    },
                    contentDescription = null,
                    tint = if (coin.isFavorite) {
                        FAVORITE_AMBER
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 12.dp).fillMaxWidth())

        Text(
            text = PriceFormatter.formatUsd(displayedPrice),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = displayedSecondary,
            style = MaterialTheme.typography.bodyMedium,
            color = secondaryColor,
        )
    }
}
