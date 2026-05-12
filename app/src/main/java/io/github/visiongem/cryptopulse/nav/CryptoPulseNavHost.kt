package io.github.visiongem.cryptopulse.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.visiongem.cryptopulse.feature.detail.CoinDetailScreen
import io.github.visiongem.cryptopulse.feature.markets.MarketsScreen
import io.github.visiongem.cryptopulse.feature.settings.SettingsScreen
import io.github.visiongem.cryptopulse.feature.watchlist.WatchlistScreen

@Composable
fun CryptoPulseNavHost(
    deepLinkCoinId: String? = null,
    onDeepLinkConsumed: () -> Unit = {},
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar = backStackEntry?.destination?.route in TopDestination.entries.map { it.route }

    LaunchedEffect(deepLinkCoinId) {
        if (!deepLinkCoinId.isNullOrEmpty()) {
            navController.navigate("detail/$deepLinkCoinId")
            onDeepLinkConsumed()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) CryptoPulseBottomBar(navController)
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TopDestination.Markets.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            composable(TopDestination.Markets.route) {
                MarketsScreen(
                    onCoinClick = { coinId -> navController.navigate("detail/$coinId") },
                )
            }
            composable(TopDestination.Watchlist.route) {
                WatchlistScreen(
                    onCoinClick = { coinId -> navController.navigate("detail/$coinId") },
                )
            }
            composable(TopDestination.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = "detail/{coinId}",
                arguments = listOf(navArgument("coinId") { type = NavType.StringType }),
            ) { entry ->
                val coinId = entry.arguments?.getString("coinId").orEmpty()
                CoinDetailScreen(
                    coinId = coinId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

@Composable
private fun CryptoPulseBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        TopDestination.entries.forEach { destination ->
            val selected = backStackEntry?.destination?.hierarchy
                ?.any { it.route == destination.route } == true ||
                currentRoute == destination.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) {
                            destination.iconSelected
                        } else {
                            destination.iconUnselected
                        },
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.labelRes)) },
            )
        }
    }
}
