package domain

import com.entourageapp.core.network.dto.EstimateItemCreateDto
import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.MeasureUnitDto
import com.entourageapp.core.network.dto.RoomShortDto

interface EstimateRepository {
    suspend fun getUnits(projectId: Int): List<MeasureUnitDto>
    suspend fun getItemTypes(projectId: Int): List<EstimateItemTypeDto>
    suspend fun getRooms(projectId: Int): List<RoomShortDto>
    suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto): Int
}