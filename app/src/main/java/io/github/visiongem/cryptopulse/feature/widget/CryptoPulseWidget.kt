package io.github.visiongem.cryptopulse.feature.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.material3.GlanceTheme
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import io.github.visiongem.cryptopulse.CryptoPulseApp
import io.github.visiongem.cryptopulse.util.PriceFormatter
import kotlinx.coroutines.flow.first

class CryptoPulseWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Responsive(
        setOf(SIZE_SMALL, SIZE_MEDIUM, SIZE_LARGE),
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val snapshot = (context.applicationContext as CryptoPulseApp)
            .serviceLocator
            .widgetDataStore
            .snapshotFlow
            .first()

        provideContent {
            GlanceTheme {
                WidgetRoot(snapshot)
            }
        }
    }

    companion object {
        val SIZE_SMALL = DpSize(120.dp, 70.dp)
        val SIZE_MEDIUM = DpSize(260.dp, 120.dp)
        val SIZE_LARGE = DpSize(260.dp, 220.dp)
    }
}

class CryptoPulseWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = CryptoPulseWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WidgetUpdateWorker.schedule(context)
        WidgetUpdateWorker.runNow(context)
    }
}

@Composable
private fun WidgetRoot(snapshot: WidgetDataSnapshot?) {
    val size = LocalSize.current
    val context = LocalContext.current
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.background)
            .cornerRadius(16.dp)
            .clickable(actionStartActivity(WidgetClickActivity::class.java)),
        contentAlignment = Alignment.Center,
    ) {
        when {
            snapshot == null || snapshot.coins.isEmpty() -> WidgetLoading()
            size.width < CryptoPulseWidget.SIZE_MEDIUM.width -> WidgetSmall(snapshot)
            size.height < CryptoPulseWidget.SIZE_LARGE.height -> WidgetMedium(snapshot)
            else -> WidgetLarge(snapshot)
        }
    }
}

@Composable
private fun WidgetLoading() {
    Column(
        modifier = GlanceModifier.fillMaxSize().padding(12.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        Text(
            text = "CryptoPulse",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = GlanceTheme.colors.onBackground,
            ),
        )
    }
}

@Composable
private fun WidgetSmall(snapshot: WidgetDataSnapshot) {
    val coin = snapshot.coins.first()
    Column(
        modifier = GlanceModifier.fillMaxSize().padding(12.dp),
    ) {
        Text(
            text = coin.symbol,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.onBackground,
            ),
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Text(
            text = PriceFormatter.formatUsd(coin.price),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = GlanceTheme.colors.onBackground,
            ),
        )
        Text(
            text = PriceFormatter.formatPercent(coin.priceChangePercentage24h),
            style = TextStyle(
                fontSize = 12.sp,
                color = signalColor(coin.priceChangePercentage24h),
            ),
        )
    }
}

@Composable
private fun WidgetMedium(snapshot: WidgetDataSnapshot) {
    Column(
        modifier = GlanceModifier.fillMaxSize().padding(12.dp),
    ) {
        Text(
            text = "CryptoPulse",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = GlanceTheme.colors.onSurfaceVariant,
            ),
        )
        Spacer(modifier = GlanceModifier.height(8.dp))
        snapshot.coins.take(3).forEach { coin ->
            CompactRow(coin)
            Spacer(modifier = GlanceModifier.height(6.dp))
        }
    }
}

@Composable
private fun WidgetLarge(snapshot: WidgetDataSnapshot) {
    Column(
        modifier = GlanceModifier.fillMaxSize().padding(12.dp),
    ) {
        Text(
            text = "CryptoPulse",
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = GlanceTheme.colors.onSurfaceVariant,
            ),
        )
        Spacer(modifier = GlanceModifier.height(10.dp))
        snapshot.coins.take(5).forEach { coin ->
            CompactRow(coin)
            Spacer(modifier = GlanceModifier.height(8.dp))
        }
    }
}

@Composable
private fun CompactRow(coin: WidgetCoinData) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.Vertical.CenterVertically,
    ) {
        Text(
            text = coin.symbol,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.onBackground,
            ),
            modifier = GlanceModifier.defaultWeight(),
        )
        Text(
            text = PriceFormatter.formatUsd(coin.price),
            style = TextStyle(
                fontSize = 13.sp,
                color = GlanceTheme.colors.onBackground,
            ),
        )
        Spacer(modifier = GlanceModifier.padding(start = 8.dp))
        Text(
            text = PriceFormatter.formatPercent(coin.priceChangePercentage24h),
            style = TextStyle(
                fontSize = 12.sp,
                color = signalColor(coin.priceChangePercentage24h),
            ),
        )
    }
}

private fun signalColor(percent: Double): ColorProvider {
    val color = if (percent >= 0) Color(0xFF10B981) else Color(0xFFEF4444)
    return ColorProvider(color)
}
