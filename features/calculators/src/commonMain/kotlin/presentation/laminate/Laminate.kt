package com.entourageapp.features.calculators.presentation.laminate

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
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.ScreenTitle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Laminate(
    roomId: Int = 0,
    projectId: Int = 0,
    viewModel: LaminateVM = koinViewModel(),
    onBackClick: () -> Unit,
    transferToEstimate: (Double) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(roomId) {
        if (roomId != 0) viewModel.handleIntent(LaminateIntent.LoadParams(projectId, roomId))
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).systemBarsPadding(),
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            title = "ЛАМИНАТ",
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
            CustomTextBar(
                value = state.floorArea,
                onValueChange = { viewModel.handleIntent(LaminateIntent.UpdateFloorArea(it)) },
                label = "Площадь пола, м²",
            )

            CustomTextBar(
                value = state.packArea,
                onValueChange = { viewModel.handleIntent(LaminateIntent.UpdatePackArea(it)) },
                label = "Площадь в упаковке, м²",
            )

            CustomTextBar(
                value = state.reserve,
                onValueChange = { viewModel.handleIntent(LaminateIntent.UpdateReserve(it)) },
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
                text = "Всего коробок",
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
                onClick = { transferToEstimate(state.result.toDouble()) },
                containerColor = EntouragePeachAlpha80,
                contentColor = EntourageBlack
            )
        }
        AccentButton(
            modifier = Modifier.fillMaxWidth().height(52.dp),
            text = "Посчитать",
            onClick = { viewModel.handleIntent(LaminateIntent.Calculate) },
            containerColor = EntourageBlack,
            contentColor = EntourageWhite
        )
    }
}
