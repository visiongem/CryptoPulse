package io.github.visiongem.cryptopulse.feature.widget

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.core.graphics.createBitmap

/**
 * Renders a price series as a smooth line into a Bitmap suitable for Glance
 * [androidx.glance.ImageProvider].
 *
 * The widget passes the resulting bitmap across the RemoteViews IPC boundary
 * (system copies it), so the caller can let the bitmap be GC-collected after
 * `Image(provider = ImageProvider(bitmap))` is set. No long-lived cache or
 * recycle dance is needed here.
 */
object SparklineRenderer {

    fun render(
        prices: List<Double>,
        widthPx: Int,
        heightPx: Int,
        @androidx.annotation.ColorInt colorInt: Int,
        strokeWidthPx: Float = DEFAULT_STROKE_WIDTH_PX,
    ): Bitmap {
        val bitmap = createBitmap(widthPx, heightPx)
        if (prices.size < 2) return bitmap

        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = colorInt
            style = Paint.Style.STROKE
            strokeWidth = strokeWidthPx
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        val min = prices.min()
        val max = prices.max()
        val range = (max - min).takeIf { it > 0 } ?: 1.0

        val padding = strokeWidthPx
        val plotHeight = heightPx - 2 * padding
        val xStep = widthPx.toFloat() / (prices.size - 1).coerceAtLeast(1)

        val path = Path()
        prices.forEachIndexed { index, price ->
            val x = index * xStep
            val y = padding + plotHeight - ((price - min) / range).toFloat() * plotHeight
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        canvas.drawPath(path, paint)
        return bitmap
    }

    private const val DEFAULT_STROKE_WIDTH_PX = 3f
}
