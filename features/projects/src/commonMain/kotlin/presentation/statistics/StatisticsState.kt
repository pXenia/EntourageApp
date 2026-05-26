package com.entourageapp.features.projects.presentation.statistics

import androidx.compose.ui.graphics.Color

data class StatisticsState(
    val spent: String = "0 ₽",
    val planned: String = "0 ₽",
    val progress: Float = 0f,
    val rooms: List<RoomData> = emptyList(),
    val summaryItems: List<SummaryItem> = emptyList(),
    val status: StatisticsStatus = StatisticsStatus.Loading
) {
    sealed interface StatisticsStatus {
        data object Loading : StatisticsStatus
        data object Error : StatisticsStatus
        data object Content : StatisticsStatus
    }
}

data class RoomData(
    val name: String,
    val matPart: Float,
    val matSum: Float,
    val compPart: Float,
    val compSum: Float,
    val laborPart: Float,
    val laborSum: Float
)

data class SummaryItem(
    val name: String,
    val percent: Float,
    val amount: String,
    val color: Color
)

sealed interface StatisticsIntent {
    data class LoadStatistics(val projectId: Int) : StatisticsIntent
}

sealed interface StatisticsSideEffect {
    data class ShowError(val message: String) : StatisticsSideEffect
}
