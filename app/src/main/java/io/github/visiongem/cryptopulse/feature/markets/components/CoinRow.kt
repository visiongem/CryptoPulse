package io.github.visiongem.cryptopulse.feature.markets.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.visiongem.cryptopulse.data.model.Coin
import io.github.visiongem.cryptopulse.ui.component.priceFlash
import io.github.visiongem.cryptopulse.ui.theme.CryptoPulseTheme
import io.github.visiongem.cryptopulse.ui.theme.SignalNegative
import io.github.visiongem.cryptopulse.ui.theme.SignalPositive
import io.github.visiongem.cryptopulse.util.PriceFormatter

private val FAVORITE_AMBER = Color(0xFFFBBF24)

@Composable
fun CoinRow(
    coin: Coin,
    rippleEnabled: Boolean,
    onFavoriteClick: (String) -> Unit,
    onClick: (Coin) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (rippleEnabled) Modifier.priceFlash(value = coin.price) else Modifier)
            .clickable { onClick(coin) }
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { onFavoriteClick(coin.symbol) }) {
            Icon(
                imageVector = if (coin.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = if (coin.isFavorite) {
                    FAVORITE_AMBER
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }

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
                text = coin.symbol,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = coin.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Column(
            modifier = Modifier.padding(end = 8.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = PriceFormatter.formatUsd(coin.price),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )
            val isPositive = coin.priceChangePercentage24h >= 0
            Text(
                text = PriceFormatter.formatPercent(coin.priceChangePercentage24h),
                style = MaterialTheme.typography.bodySmall,
                color = if (isPositive) SignalPositive else SignalNegative,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CoinRowFavoritedPreview() {
    CryptoPulseTheme {
        CoinRow(
            coin = Coin(
                id = "bitcoin",
                symbol = "BTC",
                name = "Bitcoin",
                imageUrl = "",
                price = 102345.67,
                priceChangePercentage24h = 2.345,
                marketCapRank = 1,
                isFavorite = true,
            ),
            rippleEnabled = true,
            onFavoriteClick = {},
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CoinRowUnfavoritedPreview() {
    CryptoPulseTheme {
        CoinRow(
            coin = Coin(
                id = "ethereum",
                symbol = "ETH",
                name = "Ethereum",
                imageUrl = "",
                price = 3845.12,
                priceChangePercentage24h = -1.78,
                marketCapRank = 2,
                isFavorite = false,
            ),
            rippleEnabled = true,
            onFavoriteClick = {},
            onClick = {},
        )
    }
}
