package com.entourageapp.features.rooms.presentation.createroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeachAlpha80
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.CustomDropdownBar
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.InfoLine
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.features.rooms.presentation.components.drawplan.fmt

@Composable
fun CreateRoomScreen(
    projectId: Int,
    roomId: Int? = null,
    viewModel: CreateRoomVM,
    onDrawPlanClick: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (roomId == null) {
            viewModel.handleIntent(CreateRoomIntent.LoadRoomTypes(projectId))
        } else {
            viewModel.handleIntent(CreateRoomIntent.LoadRoom(projectId, roomId))
        }
    }
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onBackClick()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth(),
            title = if (roomId == null) "Создание комнаты" else "Редактирование комнаты",
            onBackClick = onBackClick
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomTextBar(
                value = state.title,
                onValueChange = { viewModel.handleIntent(CreateRoomIntent.OnTitleChanged(it)) },
                label = "Название",
                placeholder = "Например, гостиная"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomDropdownBar(
                    modifier = Modifier.weight(1f),
                    items = state.roomTypes,
                    selectedItem = state.selectedRoomType,
                    onItemSelected = { viewModel.handleIntent(CreateRoomIntent.OnRoomTypeSelected(it)) },
                    itemLabel = { it.title },
                    label = "Тип комнаты",
                )
                CustomTextBar(
                    modifier = Modifier.weight(1f),
                    value = state.ceilingHeight,
                    onValueChange = {
                        viewModel.handleIntent(
                            CreateRoomIntent.OnCeilingHeightChanged(
                                it
                            )
                        )
                    },
                    placeholder = "270",
                    label = "Высота потолка",
                )
            }

            if (state.walls.isNotEmpty()) {
                InfoLine(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Площадь",
                    value = "${state.square.fmt()} кв. м"
                )
                state.walls.forEach { wall ->
                    InfoLine(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Стена ${wall.index}",
                        value = "${(wall.lengthM * 100).toInt()} см"
                    )
                }
            }

            AccentButton(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().height(48.dp),
                onClick = { onDrawPlanClick(projectId) },
                text = if (state.walls.isEmpty()) "Нарисовать план комнаты" else "Перерисовать план",
                containerColor = EntouragePeachAlpha80,
                contentColor = EntourageBlack
            )

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = EntourageRed,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            CustomTextBar(
                value = state.description,
                onValueChange = { viewModel.handleIntent(CreateRoomIntent.OnDescriptionChanged(it)) },
                label = "Описание",
                placeholder = "Что-то важное",
                isSingleLine = false
            )

            Spacer(modifier = Modifier.height(4.dp))

            AccentButton(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                onClick = { viewModel.handleIntent(CreateRoomIntent.Submit(projectId)) },
                text = if (state.isLoading) "Сохранение..." else if (roomId != null) "Сохранить" else "Создать",
                containerColor = EntourageBlack,
                contentColor = EntourageWhite
            )
        }
    }
}