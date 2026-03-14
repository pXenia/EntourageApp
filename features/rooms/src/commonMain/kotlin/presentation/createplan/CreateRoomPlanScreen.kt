package com.entourageapp.features.rooms.presentation.createplan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
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
    cellSizeDp: Dp = 18.dp
) {
    val state by viewModel.state.collectAsState()
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // обновление размера ячейки при смене плотности экрана
    LaunchedEffect(cellSizeDp) {
        viewModel.handleIntent(RoomDrawerIntent.UpdateCellSize(with(density) { cellSizeDp.toPx() }))
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // расчет метрики
        val area = remember(state.points, state.cellSizePx) {
            polygonAreaM2(state.points, state.cellSizePx)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EntourageBlack, RoundedCornerShape(32.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        EntourageTeal.copy(alpha = 0.2f),
                        RoundedCornerShape(32.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "Площадь " + if (state.points.size >= 3) "${area.fmt()} м²" else "— м²",
                    color = EntourageTeal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = state.snapEnabled,
                    onCheckedChange = { viewModel.handleIntent(RoomDrawerIntent.ToggleSnap(it)) },
                    colors = SwitchDefaults.colors(checkedTrackColor = EntourageTeal.copy(alpha = 0.2f))
                )
                Text(
                    text = "Привязка\nк сетке",
                    color = EntourageBlack,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        // холст
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val w = constraints.maxWidth.toFloat()
            val h = constraints.maxHeight.toFloat()

            // параметры сетки
            val majorStep = 5
            val majorPx = state.cellSizePx * majorStep
            val offX = (w - (w / majorPx).toInt() * majorPx) / 2f
            val offY = (h - (h / majorPx).toInt() * majorPx) / 2f

            fun maybeSnap(o: Offset) =
                if (state.snapEnabled) snapToGrid(o, state.cellSizePx, offX, offY) else o

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(state.mode, state.snapEnabled) {
                        detectTapGestures { tap ->
                            when (state.mode) {
                                DrawMode.DRAW -> {
                                    val pos = maybeSnap(tap)
                                    if (state.points.none { it.dst(pos) < HIT_RADIUS }) viewModel.handleIntent(
                                        RoomDrawerIntent.AddPoint(pos)
                                    )
                                }

                                DrawMode.DELETE -> {
                                    val idx =
                                        state.points.indexOfFirst { it.dst(tap) < HIT_RADIUS }
                                    if (idx != -1) viewModel.handleIntent(
                                        RoomDrawerIntent.RemovePoint(
                                            idx
                                        )
                                    )
                                }

                                else -> {}
                            }
                        }
                    }
                    .pointerInput(state.mode, state.snapEnabled) {
                        if (state.mode != DrawMode.EDIT) return@pointerInput
                        detectDragGestures(
                            onDragStart = { start ->
                                val idx =
                                    state.points.indexOfFirst { it.dst(start) < HIT_RADIUS }
                                if (idx != -1) viewModel.handleIntent(RoomDrawerIntent.DragEnd) // сброс старого индекса
                            },
                            onDrag = { change, _ ->
                                val idx =
                                    state.points.indexOfFirst { it.dst(change.previousPosition) < HIT_RADIUS * 2 }
                                if (idx != -1) {
                                    val clamped = Offset(
                                        change.position.x.coerceIn(0f, w),
                                        change.position.y.coerceIn(0f, h)
                                    )
                                    viewModel.handleIntent(
                                        RoomDrawerIntent.MovePoint(
                                            idx,
                                            maybeSnap(clamped)
                                        )
                                    )
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
                    drawPath(path, EntouragePeach.copy(alpha = 0.3f))
                }

                // стены и точки
                drawRoomContent(state, textMeasurer)
            }
        }

        // тулбар режимов
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EntourageBlack, RoundedCornerShape(32.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ModeChip("Рисование", state.mode == DrawMode.DRAW) {
                viewModel.handleIntent(
                    RoomDrawerIntent.SetMode(DrawMode.DRAW)
                )
            }
            ModeChip("Правка", state.mode == DrawMode.EDIT) {
                viewModel.handleIntent(
                    RoomDrawerIntent.SetMode(DrawMode.EDIT)
                )
            }
            ModeChip("Удаление", state.mode == DrawMode.DELETE) {
                viewModel.handleIntent(
                    RoomDrawerIntent.SetMode(DrawMode.DELETE)
                )
            }
        }
    }
}

@Composable
fun RowScope.ModeChip(label: String, active: Boolean, onClick: () -> Unit) {
    // кнопка выбора режима рисования
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active) EntourageTeal.copy(alpha = 0.2f) else EntourageWhite.copy(alpha = 0.2f),
            contentColor = if (active) EntourageTeal else EntourageBlack.copy(alpha = 0.8f)
        )
    ) {
        Text(label, fontSize = 12.sp, maxLines = 1)
    }
}
