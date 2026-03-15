package com.entourageapp.features.rooms.presentation.createroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeachAlpha80
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.CustomDropdownBar
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.InfoLine
import com.entourageapp.core.ui.components.ScreenTitle

@Composable
fun CreateRoomScreen(
    projectId: Int,
    onDrawPlanClick: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
//    LaunchedEffect(state.isSuccess) {
//        if (state.isSuccess) onBackClick()
//    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenTitle(
            modifier = modifier.fillMaxWidth().padding(bottom = 8.dp),
            title = "Создание комнаты",
            onBackClick = onBackClick
        )
        CustomTextBar(
            label = "Название",
            placeholder = "Например, гостиная"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomDropdownBar(
                modifier = Modifier.weight(1f),
                items = listOf("1", "2", "3", "4", "5"),
                selectedItem = "1",
                onItemSelected = { },
                itemLabel = { "1" },
                label = "Тип комнаты",
                placeholder = "Выберите тип"
            )
            CustomTextBar(
                modifier = Modifier.weight(1f),
                label = "Высота потолка",
                placeholder = "260.20"
            )
        }
        InfoLine(
            modifier = Modifier.fillMaxWidth(),
            label = "Площадь",
            value = "260.20 м²"
        )
        InfoLine(
            modifier = Modifier.fillMaxWidth(),
            label = "Стена 1",
            value = "100 м"
        )
        InfoLine(
            modifier = Modifier.fillMaxWidth(),
            label = "Стена 2",
            value = "90 м"
        )
        InfoLine(
            modifier = Modifier.fillMaxWidth(),
            label = "Стена 3",
            value = "100 м"
        )
        InfoLine(
            modifier = Modifier.fillMaxWidth(),
            label = "Стена 4",
            value = "90 м"
        )
        AccentButton(
            modifier = Modifier.fillMaxWidth()
                .height(48.dp),
            onClick = { onDrawPlanClick(projectId) },
            text = "Нарисовать план комнаты",
            containerColor = EntouragePeachAlpha80,
            contentColor = EntourageBlack
        )
        CustomTextBar(
            label = "Описание",
            placeholder = "Что-то важное\nЧто-то важное",
            onValueChange = { },
            modifier = Modifier.weight(1f),
            barModifier = Modifier.weight(1f),
            isSingleLine = false
        )
        Spacer(modifier = Modifier.height(4.dp))
        AccentButton(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            onClick = { },
            text = "создать",
            containerColor = EntourageBlack,
            contentColor = EntourageWhite
        )
    }
}
