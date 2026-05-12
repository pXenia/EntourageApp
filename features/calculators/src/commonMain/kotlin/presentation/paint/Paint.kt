package com.entourageapp.features.calculators.presentation.paint

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeachAlpha80
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageTealAlpha20
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.features.calculators.presentation.SelectWallsDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Paint(
    roomId: Int = 0,
    projectId: Int = 0,
    viewModel: PaintVM = koinViewModel(),
    onBackClick: () -> Unit,
    transferToEstimate: (Double) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(roomId) {
        if (roomId != 0) viewModel.handleIntent(PaintIntent.LoadParams(projectId, roomId))
    }

    if (state.showWallSelectionDialog) {
        SelectWallsDialog(
            walls = state.walls,
            selectedIds = state.selectedWallIds,
            onToggle = { viewModel.handleIntent(PaintIntent.ToggleWallSelection(it)) },
            onDismiss = { viewModel.handleIntent(PaintIntent.DismissWallSelectionDialog) }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).systemBarsPadding(),
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            title = "КРАСКА",
            onBackClick = onBackClick
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccentButton(
                    modifier = Modifier.weight(1f).height(44.dp),
                    text = "Стены",
                    onClick = { viewModel.handleIntent(PaintIntent.ToggleTarget(PaintTarget.WALLS)) },
                    containerColor = if (PaintTarget.WALLS in state.targets) EntourageBlack else EntourageWhite.copy(alpha = 0.6f),
                    contentColor = if (PaintTarget.WALLS in state.targets) EntourageWhite else EntourageBlack
                )
                AccentButton(
                    modifier = Modifier.weight(1f).height(44.dp),
                    text = "Потолок",
                    onClick = { viewModel.handleIntent(PaintIntent.ToggleTarget(PaintTarget.CEILING)) },
                    containerColor = if (PaintTarget.CEILING in state.targets) EntourageBlack else EntourageWhite.copy(alpha = 0.6f),
                    contentColor = if (PaintTarget.CEILING in state.targets) EntourageWhite else EntourageBlack
                )
            }

            if (PaintTarget.WALLS in state.targets) {
                CustomTextBar(
                    value = state.ceilingHeight,
                    onValueChange = { viewModel.handleIntent(PaintIntent.UpdateCeilingHeight(it)) },
                    label = "Высота потолка, см",
                )

                if (roomId == 0) {
                    state.manualWalls.forEachIndexed { i, value ->
                        CustomTextBar(
                            value = value,
                            onValueChange = {
                                viewModel.handleIntent(PaintIntent.UpdateManualWall(i, it))
                            },
                            label = "Стена ${i + 1}, см",
                        )
                    }
                    AccentButton(
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        text = "Добавить стену",
                        onClick = { viewModel.handleIntent(PaintIntent.AddManualWall) },
                        containerColor = EntourageTealAlpha20,
                        contentColor = EntourageBlack
                    )
                } else {
                    val label = if (state.selectedWallIds.isEmpty()) "Выбрать стены"
                    else "Выбрано стен: ${state.selectedWallIds.size}"
                    AccentButton(
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        text = label,
                        onClick = { viewModel.handleIntent(PaintIntent.ShowWallSelectionDialog) },
                        containerColor = EntourageTealAlpha20,
                        contentColor = EntourageBlack
                    )
                }
            }
            if (PaintTarget.CEILING in state.targets) {
                CustomTextBar(
                    value = state.manualArea,
                    onValueChange = { viewModel.handleIntent(PaintIntent.UpdateManualArea(it)) },
                    label = "Площадь потолка, м²",
                )
            }

            CustomTextBar(
                value = state.consumption,
                onValueChange = { viewModel.handleIntent(PaintIntent.UpdateConsumption(it)) },
                label = "Расход, м²/л",
            )

            CustomTextBar(
                value = state.layers,
                onValueChange = { viewModel.handleIntent(PaintIntent.UpdateLayers(it)) },
                label = "Количество слоев",
            )

            CustomTextBar(
                value = state.reserve,
                onValueChange = { viewModel.handleIntent(PaintIntent.UpdateReserve(it)) },
                label = "Запас, %",
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .border(1.dp, EntourageBlack, RoundedCornerShape(32.dp))
                .padding(horizontal = 22.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Всего литров",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            )
            Text(
                text = "${state.result}",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                color = EntourageTeal
            )
        }
        if (projectId != 0 && state.result != 0.0) {
            AccentButton(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).height(52.dp),
                text = "Перенести в смету",
                onClick = { transferToEstimate(state.result) },
                containerColor = EntouragePeachAlpha80,
                contentColor = EntourageBlack
            )
        }
        AccentButton(
            modifier = Modifier.fillMaxWidth().height(52.dp),
            text = "Посчитать",
            onClick = { viewModel.handleIntent(PaintIntent.Calculate) },
            containerColor = EntourageBlack,
            contentColor = EntourageWhite
        )
    }
}
