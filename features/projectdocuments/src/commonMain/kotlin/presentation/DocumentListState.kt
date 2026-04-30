package com.entourageapp.features.projectdocuments.presentation

import com.entourageapp.core.network.dto.DocumentDto

data class DocumentListState(
    val isLoading: Boolean = false,
    val documents: List<DocumentDto> = emptyList(),
    val error: String? = null
)

sealed class DocumentListIntent {
    data class LoadDocuments(val projectId: Int) : DocumentListIntent()
    data class AddDocument(val projectId: Int, val title: String, val url: String) : DocumentListIntent()
}