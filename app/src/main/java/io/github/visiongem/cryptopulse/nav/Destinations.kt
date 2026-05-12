package io.github.visiongem.cryptopulse.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.filled.Settings as FilledSettings
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.visiongem.cryptopulse.R

enum class TopDestination(
    val route: String,
    @StringRes val labelRes: Int,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
) {
    Markets(
        route = "markets",
        labelRes = R.string.nav_markets,
        iconSelected = Icons.Filled.ShowChart,
        iconUnselected = Icons.Outlined.ShowChart,
    ),
    Watchlist(
        route = "watchlist",
        labelRes = R.string.nav_watchlist,
        iconSelected = Icons.Filled.Star,
        iconUnselected = Icons.Outlined.StarOutline,
    ),
    Settings(
        route = "settings",
        labelRes = R.string.nav_settings,
        iconSelected = FilledSettings,
        iconUnselected = Icons.Outlined.Settings,
    ),
}
