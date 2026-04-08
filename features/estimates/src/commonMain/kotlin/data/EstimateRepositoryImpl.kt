package data

import com.entourageapp.core.network.api.EstimateApi
import com.entourageapp.core.network.dto.EstimateItemCreateDto
import domain.EstimateRepository

class EstimateRepositoryImpl(private val api: EstimateApi) : EstimateRepository {
    override suspend fun getUnits(projectId: Int) = api.getUnits(projectId)
    override suspend fun getItemTypes(projectId: Int) = api.getItemTypes(projectId)
    override suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto) =
        api.addEstimateItem(projectId, item)
}