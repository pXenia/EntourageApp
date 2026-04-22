package com.entourageapp.features.estimates.presentation.estimatelist

import com.entourageapp.core.network.dto.EstimateItemDto

data class EstimateListState(
    val items: List<EstimateItemDto> = emptyList(),
    val totalSum: Double = 0.0,
    val itemsCount: Int = 0,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isExporting: Boolean = false,
    val error: String? = null
) {
    val filteredItems: List<EstimateItemDto>
        get() = if (searchQuery.isBlank()) items
        else items.filter { it.name.contains(searchQuery, ignoreCase = true) }
}

sealed class EstimateListIntent {
    data class LoadData(val projectId: Int) : EstimateListIntent()
    data class UpdateSearch(val query: String) : EstimateListIntent()
    data class DeleteItem(val itemId: Int) : EstimateListIntent()
    data class ExportXlsx(val projectId: Int) : EstimateListIntent()
}