package com.entourageapp.features.projects.presentation.createproject

data class CreateProjectState(
    val title: String = "",
    val startDate: String = "",
    val endDate: String? = null,
    val square: String = "",
    val isCalculatedSquare: Boolean = false,
    val budget: String = "",
    val description: String = "",
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
    object Submit : CreateProjectIntent()
}