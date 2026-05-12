package io.github.visiongem.cryptopulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.visiongem.cryptopulse.nav.CryptoPulseNavHost
import io.github.visiongem.cryptopulse.ui.theme.CryptoPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoPulseTheme {
                CryptoPulseNavHost()
            }
        }
    }
}
