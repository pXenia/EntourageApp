package com.entourageapp.features.projects.presentation.createproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectMemberAddDto
import com.entourageapp.core.ui.tools.tryParseDate
import com.entourageapp.features.projects.domain.ProjectsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProjectVM(
    private val repository: ProjectsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateProjectState())
    val state: StateFlow<CreateProjectState> = _state

    private val _sideEffect = MutableSharedFlow<CreateProjectSideEffect>()
    val sideEffect: SharedFlow<CreateProjectSideEffect> = _sideEffect.asSharedFlow()

    fun handleIntent(intent: CreateProjectIntent) {
        when (intent) {
            is CreateProjectIntent.UpdateTitle -> _state.update {
                it.copy(title = intent.value, error = null)
            }
            is CreateProjectIntent.UpdateStartDate -> _state.update {
                it.copy(startDate = intent.value, error = null)
            }
            is CreateProjectIntent.UpdateEndDate -> _state.update {
                it.copy(endDate = intent.value, error = null)
            }
            is CreateProjectIntent.UpdateSquare -> _state.update {
                it.copy(square = intent.value)
            }
            is CreateProjectIntent.UpdateIsCalculatedSquare -> _state.update {
                it.copy(isCalculatedSquare = intent.value)
            }
            is CreateProjectIntent.UpdateBudget -> _state.update {
                it.copy(budget = intent.value)
            }
            is CreateProjectIntent.UpdateDescription -> _state.update {
                it.copy(description = intent.value)
            }
            is CreateProjectIntent.UpdateCurrentParticipantEmail -> _state.update {
                it.copy(currentParticipantEmail = intent.value)
            }
            is CreateProjectIntent.AddParticipant -> {
                viewModelScope.launch {
                    try {
                        val user = repository.checkEmail(intent.email)
                        val roleCode = if (intent.allowEdit) 2 else 3
                        val participant = PendingParticipant(
                            email = intent.email,
                            name = user.name,
                            roleCode = roleCode
                        )
                        _state.update { state ->
                            if (state.pendingParticipants.any { it.email == intent.email }) {
                                state.copy(currentParticipantEmail = "")
                            } else {
                                state.copy(
                                    pendingParticipants = state.pendingParticipants + participant,
                                    currentParticipantEmail = ""
                                )
                            }
                        }
                    } catch (e: Exception) {
                        _sideEffect.emit(CreateProjectSideEffect.ShowError("Пользователь с такой эл. почтой не найден"))
                    }
                }
            }
            is CreateProjectIntent.RemoveParticipant -> _state.update { state ->
                state.copy(
                    pendingParticipants = state.pendingParticipants.filter {
                        it.email != intent.email
                    }
                )
            }
            is CreateProjectIntent.LoadProject -> loadProject(intent.projectId)
            is CreateProjectIntent.Submit -> submitProject()
        }
    }

    private fun loadProject(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, projectId = projectId) }
            try {
                repository.getProjectById(projectId).collect { project ->
                    val members = repository.getProjectMembers(projectId)
                    _state.update { state ->
                        state.copy(
                            title = project.title,
                            startDate = project.startDateFormatted.replace(".", ""),
                            endDate = project.endDateFormatted?.replace(".", ""),
                            square = project.square?.toString() ?: "",
                            budget = project.budget.toLong().toString() ?: "",
                            description = project.description ?: "",
                            pendingParticipants = members.filter { it.roleId != 1 }.map {
                                PendingParticipant(
                                    email = it.email,
                                    name = it.name,
                                    roleCode = it.roleId
                                )
                            },
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun submitProject() {
        val currentState = _state.value
        var hasError = false

        if (currentState.title.isBlank()) {
            _state.update { it.copy(error = "Название проекта обязательно") }
            hasError = true
        }

        val parsedStartDate = tryParseDate(currentState.startDate)
        if (parsedStartDate == null) {
            _state.update { it.copy(error = "Некорректная дата начала") }
            hasError = true
        }

        val parsedEndDate = if (!currentState.endDate.isNullOrBlank()) {
            tryParseDate(currentState.endDate)
        } else null

        if (!currentState.endDate.isNullOrBlank() && (parsedEndDate == null)) {
            _state.update { it.copy(error = "Некорректная дата окончания") }
            hasError = true
        }

        if (parsedStartDate != null && parsedEndDate != null && parsedEndDate < parsedStartDate) {
            _state.update { it.copy(error = "Дата окончания не может быть раньше начала") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val dto = ProjectCreateDto(
                    title = currentState.title.trim(),
                    startDate = parsedStartDate.toString(),
                    endDate = parsedEndDate?.toString(),
                    square = currentState.square.toDoubleOrNull(),
                    budget = currentState.budget.toDoubleOrNull() ?: 0.0,
                    isSquareCalculated = currentState.isCalculatedSquare,
                    description = currentState.description.trim()
                )

                if (currentState.projectId != null) {
                    repository.updateProject(currentState.projectId, dto)
                    repository.syncProjectMembers(
                        projectId = currentState.projectId,
                        members = currentState.pendingParticipants.map {
                            ProjectMemberAddDto(
                                email = it.email,
                                roleId = it.roleCode
                            )
                        }
                    )
                } else {
                    val projectId = repository.createProject(dto)

                    currentState.pendingParticipants.forEach { participant ->
                        try {
                            repository.addProjectMember(
                                projectId = projectId,
                                email = participant.email,
                                roleCode = participant.roleCode
                            )
                        } catch (e: Exception) {
                        }
                    }
                }

                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}