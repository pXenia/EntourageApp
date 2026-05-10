package com.entourageapp.features.projectdocuments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.projectdocuments.domain.DocumentsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DocumentListVM(
    private val repository: DocumentsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DocumentListState())
    val state: StateFlow<DocumentListState> = _state

    fun handleIntent(intent: DocumentListIntent) {
        when (intent) {
            is DocumentListIntent.LoadDocuments -> loadDocuments(intent.projectId)
            is DocumentListIntent.AddDocument -> addDocument(intent.projectId, intent.title, intent.url)
        }
    }

    private fun loadDocuments(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.getDocuments(projectId)
                .catch { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
                .collect { docs ->
                    _state.update { it.copy(isLoading = false, documents = docs) }
                }
        }
    }

    private fun addDocument(projectId: Int, title: String, url: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                repository.createDocument(projectId, title, url)
                loadDocuments(projectId)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
