package io.github.visiongem.cryptopulse

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import io.github.visiongem.cryptopulse.nav.CryptoPulseNavHost
import io.github.visiongem.cryptopulse.ui.theme.CryptoPulseTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    private val deepLinkFlow = MutableStateFlow<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        deepLinkFlow.value = intent.getStringExtra(EXTRA_DEEP_LINK_COIN_ID)
        setContent {
            val deepLinkCoinId by deepLinkFlow.collectAsStateWithLifecycle()
            CryptoPulseTheme {
                CryptoPulseNavHost(
                    deepLinkCoinId = deepLinkCoinId,
                    onDeepLinkConsumed = { deepLinkFlow.value = null },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        deepLinkFlow.value = intent.getStringExtra(EXTRA_DEEP_LINK_COIN_ID)
    }

    companion object {
        const val EXTRA_DEEP_LINK_COIN_ID = "deep_link_coin_id"
    }
}
