package domain

import com.entourageapp.core.network.dto.EstimateItemCreateDto
import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.MeasureUnitDto

interface EstimateRepository {
    suspend fun getUnits(projectId: Int): List<MeasureUnitDto>
    suspend fun getItemTypes(projectId: Int): List<EstimateItemTypeDto>
    suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto): Int
}