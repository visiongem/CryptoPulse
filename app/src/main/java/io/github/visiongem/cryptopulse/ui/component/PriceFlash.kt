package io.github.visiongem.cryptopulse.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import io.github.visiongem.cryptopulse.ui.theme.SignalNegative
import io.github.visiongem.cryptopulse.ui.theme.SignalPositive

/**
 * Adds a fading colored flash to the modifier's bounds whenever [value] changes.
 *
 * Green pulse on increase, red on decrease. Skips the very first emission so
 * the initial REST-loaded price does not trigger a flash.
 *
 * Read order is important: keep this modifier **before** any `padding` so the
 * flash covers the full row.
 */
fun Modifier.priceFlash(
    value: Double,
    durationMillis: Int = DEFAULT_DURATION_MS,
    peakAlpha: Float = DEFAULT_PEAK_ALPHA,
): Modifier = composed {
    val previous = remember { mutableDoubleStateOf(value) }
    var flashColor by remember { mutableStateOf<Color?>(null) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(value) {
        val prev = previous.doubleValue
        if (prev != 0.0 && value != prev) {
            flashColor = if (value > prev) SignalPositive else SignalNegative
            alpha.snapTo(peakAlpha)
            alpha.animateTo(targetValue = 0f, animationSpec = tween(durationMillis))
            flashColor = null
        }
        previous.doubleValue = value
    }

    drawBehind {
        val currentAlpha = alpha.value
        val color = flashColor
        if (color != null && currentAlpha > 0f) {
            drawRect(color.copy(alpha = currentAlpha))
        }
    }
}

private const val DEFAULT_DURATION_MS = 800
private const val DEFAULT_PEAK_ALPHA = 0.16f
