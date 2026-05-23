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

    fun handleIntent(intent: StageIntent) {
        when (intent) {
            is StageIntent.LoadStages -> loadStages(intent.roomId)
            is StageIntent.ToggleTask -> toggleTask(intent.roomId, intent.stageId, intent.taskId)
            is StageIntent.UpdateStageStatus -> updateStageStatus(intent.roomId, intent.stageId, intent.status)
            is StageIntent.AddStage -> addStage(intent.roomId, intent.title, intent.deadline)
            is StageIntent.UpdateStage -> updateStage(intent.roomId, intent.stageId, intent.title, intent.deadline)
            is StageIntent.AddTask -> addTask(intent.roomId, intent.stageId, intent.title, intent.deadline)
            is StageIntent.ShowActionDialog -> _state.update {
                it.copy(showActionDialog = true, selectedStageId = intent.stageId, selectedItemName = intent.title)
            }
            is StageIntent.DismissActionDialog -> _state.update {
                it.copy(showActionDialog = false, selectedStageId = null, selectedItemName = "")
            }
            is StageIntent.ShowDeleteStageDialog -> _state.update { 
                it.copy(showActionDialog = false, showDeleteStageDialog = true, selectedStageId = intent.stageId, selectedItemName = intent.title)
            }
            is StageIntent.ShowDeleteTaskDialog -> _state.update { 
                it.copy(showDeleteTaskDialog = true, selectedTaskId = intent.taskId, selectedItemName = intent.title) 
            }
            is StageIntent.DismissDeleteDialog -> _state.update { 
                it.copy(showDeleteStageDialog = false, showDeleteTaskDialog = false, selectedStageId = null, selectedTaskId = null, selectedItemName = "") 
            }
            is StageIntent.DeleteStage -> deleteStage(intent.roomId)
            is StageIntent.DeleteTask -> deleteTask(intent.roomId)
        }
    }

    private fun loadStages(roomId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val stages = repository.getStages(roomId)
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        stages = stages.map { detail ->
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

    private fun addStage(roomId: Int, title: String, deadline: String) {
        viewModelScope.launch {
            runCatching {
                val deadlinePars = if (deadline.isNotBlank()) {
                    tryParseDate(deadline)
                } else null
                
                repository.createStage(
                    roomId = roomId,
                    statusId = StageStatus.NOT_STARTED.id,
                    title = title,
                    deadline = deadlinePars?.toString()
                )
            }.onSuccess {
                loadStages(roomId)
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun updateStage(roomId: Int, stageId: Int, title: String, deadline: String) {
        viewModelScope.launch {
            runCatching {
                val deadlinePars = if (deadline.isNotBlank()) {
                    tryParseDate(deadline)
                } else null

                repository.updateStage(
                    stageId = stageId,
                    title = title,
                    deadline = deadlinePars?.toString()
                )
            }.onSuccess {
                loadStages(roomId)
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun addTask(roomId: Int, stageId: Int, title: String, deadline: String) {
        viewModelScope.launch {
            runCatching {
                val deadlinePars = if (deadline.isNotBlank()) {
                    tryParseDate(deadline)
                } else null
                
                repository.createTask(
                    stageId = stageId,
                    title = title,
                    deadline = deadlinePars?.toString()
                )
            }.onSuccess {
                loadStages(roomId = roomId)
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun deleteStage(roomId: Int) {
        val stageId = _state.value.selectedStageId ?: return
        viewModelScope.launch {
            runCatching {
                repository.deleteStage(stageId)
            }.onSuccess {
                _state.update { it.copy(showDeleteStageDialog = false, selectedStageId = null, selectedItemName = "") }
                loadStages(roomId)
            }.onFailure { e ->
                _state.update { it.copy(error = e.message, showDeleteStageDialog = false) }
            }
        }
    }

    private fun deleteTask(roomId: Int) {
        val taskId = _state.value.selectedTaskId ?: return
        viewModelScope.launch {
            runCatching {
                repository.deleteTask(taskId)
            }.onSuccess {
                _state.update { it.copy(showDeleteTaskDialog = false, selectedTaskId = null, selectedItemName = "") }
                loadStages(roomId)
            }.onFailure { e ->
                _state.update { it.copy(error = e.message, showDeleteTaskDialog = false) }
            }
        }
    }

    private fun toggleTask(roomId: Int, stageId: Int, taskId: Int) {
        val stage = _state.value.stages.find { it.id == stageId }
        val task = stage?.tasks?.find { it.id == taskId } ?: return
        
        viewModelScope.launch {
            runCatching {
                repository.toggleTask(taskId, !task.isCompleted)
            }.onSuccess {
                loadStages(roomId = roomId)
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun updateStageStatus(roomId: Int, stageId: Int, status: StageStatus) {
        viewModelScope.launch {
            runCatching {
                repository.updateStageStatus(stageId, status.id)
            }.onSuccess {
                loadStages(roomId)
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}
