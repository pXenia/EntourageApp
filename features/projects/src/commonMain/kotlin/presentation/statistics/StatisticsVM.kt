package com.entourageapp.features.projects.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.tools.formatAmountWithCurrency
import com.entourageapp.features.projects.domain.ProjectsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsVM(
    private val repository: ProjectsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<StatisticsSideEffect>()
    val sideEffect: SharedFlow<StatisticsSideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: StatisticsIntent) {
        when (intent) {
            is StatisticsIntent.LoadStatistics -> loadStatistics(intent.projectId)
        }
    }

    private fun loadStatistics(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(status = StatisticsState.StatisticsStatus.Loading) }

            repository.getProjectSummary(projectId)
                .catch { e ->
                    _state.update { it.copy(status = StatisticsState.StatisticsStatus.Error) }
                    _sideEffect.emit(StatisticsSideEffect.ShowError("Ошибка при загрузке статистики"))
                }
                .collect { summary ->
                    val rooms = summary.rooms.map { room ->
                        val mat = room.byCategory.find { it.category == "Материал" }
                        val comp = room.byCategory.find { it.category == "Комплектующее" }
                        val labor = room.byCategory.find { it.category == "Работа" }

                        val matPart = if (room.roomTotal > 0) (mat?.total?.toFloat()
                            ?: 0f) / room.roomTotal.toFloat() else 0f
                        val compPart = if (room.roomTotal > 0) (comp?.total?.toFloat()
                            ?: 0f) / room.roomTotal.toFloat() else 0f
                        val laborPart = if (room.roomTotal > 0) 1f - matPart - compPart else 0f

                        RoomData(
                            name = room.roomTitle,
                            matPart = matPart,
                            matSum = (mat?.total ?: 0.0).toFloat(),
                            compPart = compPart,
                            compSum = (comp?.total ?: 0.0).toFloat(),
                            laborPart = laborPart,
                            laborSum = (labor?.total ?: 0.0).toFloat()
                        )
                    }

                    val summaryItems = summary.byCategory.map { category ->
                        SummaryItem(
                            name = category.category,
                            percent = if (summary.totalSpent > 0) (category.total.toFloat() / summary.totalSpent.toFloat()) else 0f,
                            amount = formatAmountWithCurrency(category.total.toFloat()),
                            color = when (category.category) {
                                "Материал" -> EntourageTeal
                                "Комплектующее" -> EntouragePeach
                                else -> EntourageBlack
                            }
                        )
                    }

                    _state.update {
                        it.copy(
                            spent = formatAmountWithCurrency(summary.totalSpent.toFloat()),
                            planned = formatAmountWithCurrency((summary.budget ?: 0f).toFloat()),
                            progress = if ((summary.budget
                                    ?: 0.0) > 0
                            ) (summary.totalSpent / summary.budget!!).toFloat() else 0f,
                            rooms = rooms,
                            summaryItems = summaryItems,
                            status = StatisticsState.StatisticsStatus.Content
                        )
                    }
                }
        }
    }
}
