package com.entourageapp.features.calculators.presentation.wallpaper

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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun Wallpaper(
    roomId: Int = 0,
    projectId: Int = 0,
    viewModel: WallpaperVM = koinViewModel(),
    onBackClick: () -> Unit,
    transferToEstimate: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(roomId) {
        if (roomId != 0) viewModel.handleIntent(WallpaperIntent.LoadParams(projectId, roomId))
    }

    if (state.showWallSelectionDialog) {
        SelectWallsDialog(
            walls = state.walls,
            selectedIds = state.selectedWallIds,
            onToggle = { viewModel.handleIntent(WallpaperIntent.ToggleWallSelection(it)) },
            onDismiss = { viewModel.handleIntent(WallpaperIntent.DismissWallSelectionDialog) }
        )
    }

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .imePadding()
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            title = "ОБОИ",
            onBackClick = onBackClick
        )
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomTextBar(
                value = state.ceilingHeight,
                onValueChange = { viewModel.handleIntent(WallpaperIntent.UpdateCeilingHeight(it)) },
                label = "Высота потолка, см",
            )

            if (roomId == 0) {
                state.manualWalls.forEachIndexed { i, value ->
                    CustomTextBar(
                        value = value,
                        onValueChange = {
                            viewModel.handleIntent(WallpaperIntent.UpdateManualWall(i, it))
                        },
                        label = "Стена ${i + 1}, см",
                    )
                }
                AccentButton(
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    text = "Добавить стену",
                    onClick = { viewModel.handleIntent(WallpaperIntent.AddManualWall) },
                    containerColor = EntourageTealAlpha20,
                    contentColor = EntourageBlack
                )
            } else {
                val label = if (state.selectedWallIds.isEmpty()) "Выбрать стены"
                else "Выбрано стен: ${state.selectedWallIds.size}"
                AccentButton(
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    text = label,
                    onClick = { viewModel.handleIntent(WallpaperIntent.ShowWallSelectionDialog) },
                    containerColor = EntourageTealAlpha20,
                    contentColor = EntourageBlack
                )
            }

            CustomTextBar(
                value = state.rollLength,
                onValueChange = { viewModel.handleIntent(WallpaperIntent.UpdateRollLength(it)) },
                label = "Длина рулона, см",
            )

            CustomTextBar(
                value = state.rollWidth,
                onValueChange = { viewModel.handleIntent(WallpaperIntent.UpdateRollWidth(it)) },
                label = "Ширина рулона, см",
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Switch(
                    checked = state.hasPatternRepeat,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = EntourageTeal,
                        uncheckedTrackColor = Color.Transparent,
                        uncheckedThumbColor = EntourageTeal
                    ),
                    onCheckedChange = { viewModel.handleIntent(WallpaperIntent.TogglePatternRepeat) }
                )
                Text(
                    text = "Есть повтор рисунка".uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp
                )
            }

            if (state.hasPatternRepeat) {
                CustomTextBar(
                    value = state.patternRepeat,
                    onValueChange = {
                        viewModel.handleIntent(
                            WallpaperIntent.UpdatePatternRepeat(
                                it
                            )
                        )
                    },
                    label = "Повтор рисунка, см",
                )
            }

            CustomTextBar(
                value = state.reserve,
                onValueChange = { viewModel.handleIntent(WallpaperIntent.UpdateReserve(it)) },
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
                text = "Всего рулонов",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            )
            Text(
                text = "${state.result}",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                color = EntourageTeal
            )
        }
        if (projectId != 0 && state.result != 0) {
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
            onClick = { viewModel.handleIntent(WallpaperIntent.Calculate) },
            containerColor = EntourageBlack,
            contentColor = EntourageWhite
        )
    }
}