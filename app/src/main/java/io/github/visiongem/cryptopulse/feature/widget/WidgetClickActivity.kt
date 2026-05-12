package io.github.visiongem.cryptopulse.feature.widget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.github.visiongem.cryptopulse.MainActivity

/**
 * Transparent dispatch Activity for widget clicks.
 *
 * Using [actionStartActivity] directly to MainActivity works on most ROMs but
 * fails on HarmonyOS / EMUI when the click handler runs inside the widget
 * BroadcastReceiver (background-start restriction). Going through this no-UI
 * Activity uses an Activity-type PendingIntent, which the system starts from
 * the RemoteViews layer — bypassing the restriction.
 */
class WidgetClickActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            },
        )
        finish()
    }
}
