package com.entourageapp.features.rooms.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.tools.drawSegmentText
import kotlin.math.sqrt

@OptIn(ExperimentalTextApi::class)
@Composable
fun RoomPlan(
    points: List<Offset>,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        if (points.isEmpty() || points.size <= 2) return@Canvas

        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }

        val dataWidth = maxX - minX
        val dataHeight = maxY - minY

        val padding = 20f
        val availableWidth = size.width - padding * 2
        val availableHeight = size.height - padding * 2

        val scale = minOf(
            if (dataWidth > 0) availableWidth / dataWidth else 1f,
            if (dataHeight > 0) availableHeight / dataHeight else 1f
        )

        val offsetX = padding + (availableWidth - dataWidth * scale) / 2f
        val offsetY = padding + (availableHeight - dataHeight * scale) / 2f

        fun transform(offset: Offset): Offset {
            return Offset(
                x = (offset.x - minX) * scale + offsetX,
                y = (offset.y - minY) * scale + offsetY
            )
        }

        val roomPath = Path().apply {
            val firstPoint = transform(points[0])
            moveTo(firstPoint.x, firstPoint.y)
            for (i in 1 until points.size) {
                lineTo(transform(points[i]).x, transform(points[i]).y)
            }
            close()
        }

        drawPath(path = roomPath, color = EntourageTeal.copy(alpha = 0.2f))

        drawPath(
            path = roomPath,
            color = EntourageTeal,
            style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        for (i in points.indices) {
            val start = transform(points[i])
            val end = transform(points[(i + 1) % points.size])

            val wallCenter = Offset(
                x = (start.x + end.x) / 2f,
                y = (start.y + end.y) / 2f
            )

            val dx = end.x - start.x
            val dy = end.y - start.y
            val length = sqrt((dx * dx + dy * dy).toDouble()).toFloat()

            val nx = -dy / length
            val ny = dx / length
            val textDistance = 25f

            val labelPos = Offset(
                x = wallCenter.x + nx * textDistance,
                y = wallCenter.y + ny * textDistance
            )

            val textLayoutResult = textMeasurer.measure(
                text = AnnotatedString((i + 1).toString()),
                style = textStyle
            )

            drawSegmentText(
                width = size.width,
                textLayoutResult = textLayoutResult,
                center = labelPos
            )
        }
    }
}