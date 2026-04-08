package com.entourageapp.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object AuthGraph : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Registration : Route

    @Serializable
    data object ProjectList : Route

    @Serializable
    data object CalculatorsList : Route

    @Serializable
    data object UserProfile : Route

    @Serializable
    data class ProjectDetail(val projectId: Int) : Route

    @Serializable
    data object CreateProject : Route

    @Serializable
    data class EstimateList(val projectId: Int) : Route
    @Serializable
    data class CreateEstimatePosition(val projectId: Int) : Route

    @Serializable
    data class RoomList(val projectId: Int) : Route

    @Serializable
    data class RoomInfo(val projectId: Int) : Route

    @Serializable
    data class CreateRoomPlan(val projectId: Int) : Route

    @Serializable
    data class CreateRoom(val projectId: Int) : Route
}
