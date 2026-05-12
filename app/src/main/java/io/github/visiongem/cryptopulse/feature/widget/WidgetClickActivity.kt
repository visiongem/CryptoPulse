package io.github.visiongem.cryptopulse.feature.widget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.github.visiongem.cryptopulse.MainActivity

/**
 * Transparent dispatch Activity for widget clicks.
 *
 * Glance fires us via `actionStartActivity(Intent)` with a URI like
 * `cryptopulse://coin/{id}` (or `cryptopulse://home`). We parse the URI and
 * relay to [MainActivity] with the coin id as an extra. Going through this
 * no-UI Activity bypasses HarmonyOS / EMUI's background-start restrictions
 * that affect direct BroadcastReceiver→startActivity paths.
 */
class WidgetClickActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val coinId = intent.data
            ?.takeIf { it.scheme == SCHEME && it.host == HOST_COIN }
            ?.lastPathSegment

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (coinId != null) {
                putExtra(MainActivity.EXTRA_DEEP_LINK_COIN_ID, coinId)
            }
        }
        startActivity(mainIntent)
        finish()
    }

    private companion object {
        const val SCHEME = "cryptopulse"
        const val HOST_COIN = "coin"
    }
}
