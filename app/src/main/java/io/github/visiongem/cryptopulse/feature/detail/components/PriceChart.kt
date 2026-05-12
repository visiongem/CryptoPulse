package io.github.visiongem.cryptopulse.feature.detail.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.github.visiongem.cryptopulse.data.model.ChartData
import io.github.visiongem.cryptopulse.data.model.ChartPoint
import io.github.visiongem.cryptopulse.ui.theme.SignalNegative
import io.github.visiongem.cryptopulse.ui.theme.SignalPositive

/**
 * Minimal line chart with crosshair on touch.
 *
 * @param onCrosshairChange invoked with the data point under the crosshair, or
 *  null when no finger is on the chart. Use to drive an external tooltip.
 */
@Composable
fun PriceChart(
    data: ChartData,
    onCrosshairChange: (ChartPoint?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var crosshairX by remember(data) { mutableStateOf<Float?>(null) }
    val lineColor = if (data.isPositive) SignalPositive else SignalNegative

    Canvas(
        modifier = modifier.pointerInput(data) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Main)
                    val pointer = event.changes.firstOrNull() ?: continue
                    if (pointer.pressed) {
                        val x = pointer.position.x.coerceIn(0f, size.width.toFloat())
                        crosshairX = x
                        onCrosshairChange(findNearestPoint(x, data, size.width.toFloat()))
                        pointer.consume()
                    } else {
                        crosshairX = null
                        onCrosshairChange(null)
                    }
                }
            }
        },
    ) {
        if (data.points.size < 2) return@Canvas
        drawPriceLine(data, lineColor)
        drawGradientFill(data, lineColor)
        crosshairX?.let { x ->
            findNearestPoint(x, data, size.width)?.let { point ->
                drawCrosshair(point, data, lineColor)
            }
        }
    }
}

private fun DrawScope.drawPriceLine(data: ChartData, color: Color) {
    drawPath(
        path = buildPath(data),
        color = color,
        style = Stroke(
            width = 2.dp.toPx(),
            cap = StrokeCap.Round,
        ),
    )
}

private fun DrawScope.drawGradientFill(data: ChartData, color: Color) {
    val line = buildPath(data)
    val fill = Path().apply {
        addPath(line)
        lineTo(size.width - HORIZONTAL_PADDING_PX, size.height - VERTICAL_PADDING_PX)
        lineTo(HORIZONTAL_PADDING_PX, size.height - VERTICAL_PADDING_PX)
        close()
    }
    drawPath(
        path = fill,
        brush = Brush.verticalGradient(
            colors = listOf(color.copy(alpha = 0.18f), Color.Transparent),
            startY = 0f,
            endY = size.height,
        ),
    )
}

private fun DrawScope.buildPath(data: ChartData): Path {
    val path = Path()
    val range = (data.maxPrice - data.minPrice).takeIf { it > 0 } ?: 1.0
    val plotWidth = size.width - 2 * HORIZONTAL_PADDING_PX
    val plotHeight = size.height - 2 * VERTICAL_PADDING_PX
    val step = plotWidth / (data.points.size - 1).coerceAtLeast(1)
    data.points.forEachIndexed { i, point ->
        val x = HORIZONTAL_PADDING_PX + i * step
        val normalized = ((point.price - data.minPrice) / range).toFloat()
        val y = size.height - VERTICAL_PADDING_PX - normalized * plotHeight
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    return path
}

private fun DrawScope.drawCrosshair(
    point: ChartPoint,
    data: ChartData,
    color: Color,
) {
    val range = (data.maxPrice - data.minPrice).takeIf { it > 0 } ?: 1.0
    val plotWidth = size.width - 2 * HORIZONTAL_PADDING_PX
    val plotHeight = size.height - 2 * VERTICAL_PADDING_PX
    val step = plotWidth / (data.points.size - 1).coerceAtLeast(1)
    val index = data.points.indexOf(point).coerceAtLeast(0)
    val x = HORIZONTAL_PADDING_PX + index * step
    val normalized = ((point.price - data.minPrice) / range).toFloat()
    val y = size.height - VERTICAL_PADDING_PX - normalized * plotHeight

    drawLine(
        color = color.copy(alpha = 0.35f),
        start = Offset(x, VERTICAL_PADDING_PX),
        end = Offset(x, size.height - VERTICAL_PADDING_PX),
        strokeWidth = 1.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f)),
    )
    drawCircle(color = color, radius = 5.dp.toPx(), center = Offset(x, y))
    drawCircle(color = Color.White, radius = 2.dp.toPx(), center = Offset(x, y))
}

private fun findNearestPoint(
    x: Float,
    data: ChartData,
    width: Float,
): ChartPoint? {
    if (data.points.isEmpty()) return null
    val plotWidth = width - 2 * HORIZONTAL_PADDING_PX
    val step = plotWidth / (data.points.size - 1).coerceAtLeast(1)
    val rawIndex = ((x - HORIZONTAL_PADDING_PX) / step).toInt()
    val index = rawIndex.coerceIn(0, data.points.size - 1)
    return data.points[index]
}

private const val HORIZONTAL_PADDING_PX = 16f
private const val VERTICAL_PADDING_PX = 24f
