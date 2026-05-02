package com.entourageapp.features.projects.presentation.createproject

data class PendingParticipant(
    val email: String,
    val name: String,
    val roleCode: String
)

data class CreateProjectState(
    val title: String = "",
    val startDate: String = "",
    val endDate: String? = null,
    val square: String = "",
    val isCalculatedSquare: Boolean = false,
    val budget: String = "",
    val description: String = "",
    val currentParticipantEmail: String = "",
    val pendingParticipants: List<PendingParticipant> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed class CreateProjectIntent {
    data class UpdateTitle(val value: String) : CreateProjectIntent()
    data class UpdateStartDate(val value: String) : CreateProjectIntent()
    data class UpdateEndDate(val value: String?) : CreateProjectIntent()
    data class UpdateSquare(val value: String) : CreateProjectIntent()
    data class UpdateIsCalculatedSquare(val value: Boolean) : CreateProjectIntent()
    data class UpdateBudget(val value: String) : CreateProjectIntent()
    data class UpdateDescription(val value: String) : CreateProjectIntent()
    data class UpdateCurrentParticipantEmail(val value: String) : CreateProjectIntent()
    data class AddParticipant(val email: String, val allowEdit: Boolean) : CreateProjectIntent()
    data class RemoveParticipant(val email: String) : CreateProjectIntent()
    object Submit : CreateProjectIntent()
}

sealed interface CreateProjectSideEffect {
    data class ShowError(val message: String) : CreateProjectSideEffect
}