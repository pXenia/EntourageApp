package com.entourageapp.features.rooms.presentation.components.drawplan

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.features.rooms.presentation.createplan.DrawMode
import com.entourageapp.features.rooms.presentation.createplan.RoomDrawerState
import kotlin.math.PI
import kotlin.math.atan2

private const val POINT_RADIUS = 14f  // радиус точки
private const val HIT_RADIUS   = 36f  // радиус касания

fun DrawScope.drawGridCentered(w: Float, h: Float, cellSize: Float, step: Int, offX: Float, offY: Float) {
    // расчет количества крупных блоков
    val majorCols = ((w - 2 * offX) / (cellSize * step)).toInt()
    val majorRows = ((h - 2 * offY) / (cellSize * step)).toInt()

    // цвета
    val gridMajor = EntourageWhite
    val gridMinor = EntourageWhite.copy(alpha = 0.5f)

    // отрисовка вертикальных линий
    for (c in 0..(majorCols * step)) {
        val x = offX + c * cellSize
        val isMajor = c % step == 0
        drawLine(
            color = if (isMajor) gridMajor else gridMinor,
            start = Offset(x, offY),
            end = Offset(x, h - offY),
            strokeWidth = if (isMajor) 1.5f else 0.8f
        )
    }
    // отрисовка горизонтальных линий
    for (r in 0..(majorRows * step)) {
        val y = offY + r * cellSize
        val isMajor = r % step == 0
        drawLine(
            color = if (isMajor) gridMajor else gridMinor,
            start = Offset(offX, y),
            end = Offset(w - offX, y),
            strokeWidth = if (isMajor) 1.5f else 0.8f
        )
    }
}

fun DrawScope.drawWallLabel(measurer: TextMeasurer, label: String, cx: Float, cy: Float, angle: Float) {
    // подготовка стиля и замер габаритов текста
    val style = TextStyle(color = EntourageLightBlueGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    val measured = measurer.measure(label, style)
    val tw = measured.size.width.toFloat()
    val th = measured.size.height.toFloat()

    // расчет угла поворота и корректировка перевернутого текста
    var rotation = angle * (180f / PI.toFloat())
    if (rotation > 90f || rotation < -90f) rotation += 180f

    // отрисовка подложки и текста с поворотом
    withTransform({ rotate(rotation, Offset(cx, cy)) }) {
        drawRoundRect(
            color = EntourageTeal,
            topLeft = Offset(cx - tw / 2 - 4, cy - th / 2 - 2),
            size = Size(tw + 8, th + 4),
            cornerRadius = CornerRadius(4f)
        )
        drawText(measured, topLeft = Offset(cx - tw / 2, cy - th / 2))
    }
}

@Composable
fun RowScope.ModeChip(label: String, active: Boolean, color: Color, onClick: () -> Unit) {
    // кнопка выбора режима рисования
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active) color.copy(alpha = 0.15f) else Color.Transparent,
            contentColor = if (active) color else EntourageLightBlueGray.copy(alpha = 0.4f)
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(label, fontSize = 11.sp, maxLines = 1)
    }
}

fun DrawScope.drawRoomContent(state: RoomDrawerState, measurer: TextMeasurer) {
    val edges = if (state.points.size >= 3) state.points.size else if (state.points.size == 2) 1 else 0

    // отрисовка линий стен
    for (i in 0 until edges) {
        val a = state.points[i]
        val b = state.points[(i + 1) % state.points.size]
        drawLine(EntourageTeal, a, b, strokeWidth = 2.5f, cap = StrokeCap.Round)

        val len = wallLenM(a, b, state.cellSizePx)
        val angle = atan2(b.y - a.y, b.x - a.x)
        drawWallLabel(measurer, "${len.fmt()} м", (a.x + b.x) / 2, (a.y + b.y) / 2, angle)
    }

    // отрисовка узлов (точек)
    state.points.forEachIndexed { i, p ->
        val color = when {
            state.mode == DrawMode.DELETE -> EntourageRed
            state.dragIndex == i -> EntourageWhite
            state.mode == DrawMode.EDIT -> EntouragePeach
            else -> EntourageTeal
        }
        drawCircle(color.copy(alpha = 0.2f), HIT_RADIUS, p)
        drawCircle(color, POINT_RADIUS, p)
    }
}