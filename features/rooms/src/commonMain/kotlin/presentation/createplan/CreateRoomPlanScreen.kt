package com.entourageapp.features.rooms.presentation.createplan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.features.rooms.presentation.components.drawplan.ModeChip
import com.entourageapp.features.rooms.presentation.components.drawplan.drawGridCentered
import com.entourageapp.features.rooms.presentation.components.drawplan.drawRoomContent
import com.entourageapp.features.rooms.presentation.components.drawplan.dst
import com.entourageapp.features.rooms.presentation.components.drawplan.fmt
import com.entourageapp.features.rooms.presentation.components.drawplan.polygonAreaM2
import com.entourageapp.features.rooms.presentation.components.drawplan.snapToGrid
import org.koin.compose.viewmodel.koinViewModel

private const val POINT_RADIUS = 14f  // радиус точки
private const val HIT_RADIUS   = 36f  // радиус касания

@Composable
fun CreateRoomPlanScreen(
    viewModel: CreateRoomPlanVM = koinViewModel(),
    cellSizeDp: Dp = 24.dp
) {
    val state by viewModel.state.collectAsState()
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // обновление размера ячейки при смене плотности экрана
    LaunchedEffect(cellSizeDp) {
        viewModel.handleIntent(RoomDrawerIntent.UpdateCellSize(with(density) { cellSizeDp.toPx() }))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(14.dp))

        // расчет метрики
        val area = remember(state.points, state.cellSizePx) {
            polygonAreaM2(state.points, state.cellSizePx)
        }

        // заголовок
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Планировщик комнаты", color = EntourageWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("1 клетка = 50 см", color = EntourageLightBlueGray.copy(alpha = 0.4f), fontSize = 11.sp)
            }
            Box(
                modifier = Modifier
                    .background(if (state.points.size >= 3) EntourageTeal.copy(alpha = 0.18f) else Color.Transparent, RoundedCornerShape(8.dp))
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    if (state.points.size >= 3) "${area.fmt()} м²" else "— м²",
                    color = if (state.points.size >= 3) EntourageTeal else EntourageLightBlueGray.copy(alpha = 0.25f),
                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // тулбар режимов
        Row(modifier = Modifier.fillMaxWidth().background(Color.Transparent, RoundedCornerShape(10.dp)).padding(6.dp)) {
            ModeChip("✏️ Рисование", state.mode == DrawMode.DRAW, EntourageTeal) { viewModel.handleIntent(RoomDrawerIntent.SetMode(DrawMode.DRAW)) }
            ModeChip("✥ Правка", state.mode == DrawMode.EDIT, EntouragePeach) { viewModel.handleIntent(RoomDrawerIntent.SetMode(DrawMode.EDIT)) }
            ModeChip("✕ Удаление", state.mode == DrawMode.DELETE, EntourageRed) { viewModel.handleIntent(RoomDrawerIntent.SetMode(DrawMode.DELETE)) }
        }

        Spacer(Modifier.height(8.dp))

        // настройки
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.Transparent, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Switch(checked = state.snapEnabled, onCheckedChange = { viewModel.handleIntent(RoomDrawerIntent.ToggleSnap(it)) }, colors = SwitchDefaults.colors(checkedTrackColor = EntourageTeal))
                Text("Привязка", color = EntourageLightBlueGray, fontSize = 13.sp)
            }
            TextButton(onClick = { viewModel.handleIntent(RoomDrawerIntent.ClearAll) }) {
                Text("Очистить", color = EntourageLightBlueGray.copy(alpha = 0.35f), fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(10.dp))

        // ── холст ──────────────────────────────────────────────────────
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().weight(1f).background(Color(0xFF0F1315), RoundedCornerShape(14.dp))) {
            val w = constraints.maxWidth.toFloat()
            val h = constraints.maxHeight.toFloat()

            // параметры сетки
            val majorStep = 5
            val majorPx = state.cellSizePx * majorStep
            val offX = (w - (w / majorPx).toInt() * majorPx) / 2f
            val offY = (h - (h / majorPx).toInt() * majorPx) / 2f

            fun maybeSnap(o: Offset) = if (state.snapEnabled) snapToGrid(o, state.cellSizePx, offX, offY) else o

            Canvas(
                modifier = Modifier.fillMaxSize()
                    .pointerInput(state.mode, state.snapEnabled) {
                        detectTapGestures { tap ->
                            when (state.mode) {
                                DrawMode.DRAW -> {
                                    val pos = maybeSnap(tap)
                                    if (state.points.none { it.dst(pos) < HIT_RADIUS }) viewModel.handleIntent(RoomDrawerIntent.AddPoint(pos))
                                }
                                DrawMode.DELETE -> {
                                    val idx = state.points.indexOfFirst { it.dst(tap) < HIT_RADIUS }
                                    if (idx != -1) viewModel.handleIntent(RoomDrawerIntent.RemovePoint(idx))
                                }
                                else -> {}
                            }
                        }
                    }
                    .pointerInput(state.mode, state.snapEnabled) {
                        if (state.mode != DrawMode.EDIT) return@pointerInput
                        detectDragGestures(
                            onDragStart = { start ->
                                val idx = state.points.indexOfFirst { it.dst(start) < HIT_RADIUS }
                                if (idx != -1) viewModel.handleIntent(RoomDrawerIntent.DragEnd) // сброс старого индекса
                            },
                            onDrag = { change, _ ->
                                val idx = state.points.indexOfFirst { it.dst(change.previousPosition) < HIT_RADIUS * 2 }
                                if (idx != -1) {
                                    val clamped = Offset(change.position.x.coerceIn(0f, w), change.position.y.coerceIn(0f, h))
                                    viewModel.handleIntent(RoomDrawerIntent.MovePoint(idx, maybeSnap(clamped)))
                                }
                            },
                            onDragEnd = { viewModel.handleIntent(RoomDrawerIntent.DragEnd) }
                        )
                    }
            ) {
                // визуализация
                drawGridCentered(w, h, state.cellSizePx, majorStep, offX, offY)

                if (state.points.size >= 3) {
                    val path = Path().apply {
                        moveTo(state.points[0].x, state.points[0].y)
                        state.points.drop(1).forEach { lineTo(it.x, it.y) }
                        close()
                    }
                    drawPath(path, EntourageTeal)
                }

                // стены и точки
                drawRoomContent(state, textMeasurer)
            }
        }

        Text(text = "Точек: ${state.points.size}", color = EntourageLightBlueGray.copy(alpha = 0.3f), fontSize = 11.sp, modifier = Modifier.padding(vertical = 10.dp))
    }
}

