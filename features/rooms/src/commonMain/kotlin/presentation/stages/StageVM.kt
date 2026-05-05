package com.entourageapp.features.rooms.presentation.stages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.ui.tools.formatDate
import com.entourageapp.core.ui.tools.tryParseDate
import com.entourageapp.features.rooms.domain.StagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StageVM(
    private val repository: StagesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(StageState())
    val state: StateFlow<StageState> = _state.asStateFlow()

    init {
        handleIntent(StageIntent.LoadStages)
    }

    fun handleIntent(intent: StageIntent) {
        when (intent) {
            is StageIntent.LoadStages -> loadStages()
            is StageIntent.ToggleTask -> toggleTask(intent.stageId, intent.taskId)
            is StageIntent.UpdateStageStatus -> updateStageStatus(intent.stageId, intent.status)
            is StageIntent.AddStage -> addStage(intent.title, intent.deadline)
            is StageIntent.AddTask -> addTask(intent.stageId, intent.title, intent.deadline)
        }
    }

    private fun loadStages() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val stages = repository.getStages()
                val stageDetails = stages.map { stage ->
                    repository.getStageDetail(stage.id)
                }
                
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        stages = stageDetails.map { detail ->
                            Stage(
                                id = detail.id,
                                title = detail.title,
                                status = StageStatus.fromId(detail.statusId),
                                deadline = formatDate(detail.deadline ?: ""),
                                tasks = detail.tasks.map { task ->
                                    Task(
                                        id = task.id,
                                        title = task.title,
                                        isCompleted = task.isCompleted,
                                        deadline = formatDate(task.deadline ?: "")
                                    )
                                }
                            )
                        }
                    )
                }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun addStage(title: String, deadline: String) {
        viewModelScope.launch {
            runCatching {
                val deadlinePars = if (deadline.isNotBlank()) {
                    tryParseDate(deadline)
                } else null
                if (deadlinePars != null)
                    repository.createStage(statusId = 3, title = title, deadline = deadlinePars.toString())
                else
                    return@launch
            }.onSuccess {
                loadStages()
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun addTask(stageId: Int, title: String, deadline: String) {
        viewModelScope.launch {
            runCatching {
                val deadlinePars = if (deadline.isNotBlank()) {
                    tryParseDate(deadline)
                } else null
                if (deadlinePars != null)
                    repository.createTask(stageId = stageId, title = title, deadline = deadlinePars.toString())
                else
                    return@launch
            }.onSuccess {
                loadStages()
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun toggleTask(stageId: Int, taskId: Int) {
        val stage = _state.value.stages.find { it.id == stageId }
        val task = stage?.tasks?.find { it.id == taskId } ?: return
        
        viewModelScope.launch {
            runCatching {
                repository.toggleTask(stageId, taskId, !task.isCompleted)
            }.onSuccess {
                loadStages()
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun updateStageStatus(stageId: Int, status: StageStatus) {
        viewModelScope.launch {
            runCatching {
                repository.updateStageStatus(stageId, status.id)
            }.onSuccess {
                loadStages()
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}