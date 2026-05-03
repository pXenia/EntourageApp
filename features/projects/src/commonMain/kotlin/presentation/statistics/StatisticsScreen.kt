package com.entourageapp.features.projects.presentation.statistics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.core.ui.components.SectionTitle
import com.entourageapp.features.projects.presentation.components.BarChart
import com.entourageapp.features.projects.presentation.components.BudgetOverview
import com.entourageapp.features.projects.presentation.components.BudgetSummaryTable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit = {},
    projectId: Int,
    viewModel: StatisticsVM = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(projectId) {
        viewModel.onIntent(StatisticsIntent.LoadStatistics(projectId))
        animationProgress.animateTo(
            targetValue = 3f, animationSpec = tween(durationMillis = 3000)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        ScreenTitle(
            title = "Статистика проекта",
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .verticalScroll(rememberScrollState())
                .padding(top = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BudgetOverview(
                spent = state.spent,
                planned = state.planned,
                progress = state.progress,
                animationP = animationProgress.value
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SectionTitle("Затраты по комнатам")

                state.rooms.forEach { room ->
                    Column {
                        Text(
                            text = room.name,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            modifier = Modifier.padding(bottom = 1.dp)
                        )

                        BarChart(
                            materials = room.matPart,
                            materialsSum = room.matSum,
                            components = room.compPart,
                            componentsSum = room.compSum,
                            labor = room.laborPart,
                            laborSum = room.laborSum,
                            animationP = animationProgress.value
                        )
                    }
                }
            }

            BudgetSummaryTable(items = state.summaryItems)
        }
    }
}
