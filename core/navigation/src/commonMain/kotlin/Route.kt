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
    data class CalculatorsList (val projectId: Int = 0, val roomId: Int = 0) : Route

    @Serializable
    data object UserProfile : Route

    @Serializable
    data object ManageProjects: Route

    @Serializable
    data class ProjectsStats(val projectId: Int): Route

    @Serializable
    data class ProjectDetail(val projectId: Int) : Route

    @Serializable
    data class ProjectInfo(val projectId: Int) : Route

    @Serializable
    data class CreateProject(val projectId: Int? = null) : Route

    @Serializable
    data class EstimateList(val projectId: Int, val roomId: Int = 0) : Route
    @Serializable
    data class CreateEstimatePosition(val projectId: Int = 0, val roomId: Int = 0, val amount: Int = 0) : Route

    @Serializable
    data class RoomList(val projectId: Int) : Route
    @Serializable
    data class RoomInfo(val projectId: Int, val roomId: Int) : Route

    @Serializable
    data class StageList(val roomId: Int) : Route

    @Serializable
    data class RoomParameters(val projectId: Int, val roomId: Int) : Route

    @Serializable
    data class RoomGraph(val projectId: Int, val roomId: Int? = null) : Route
    @Serializable sealed interface CreateRoom : NavKey {
        @Serializable data class CreateForm(val projectId: Int, val roomId: Int? = null) : CreateRoom
        @Serializable data class CreatePlan(val projectId: Int) : CreateRoom
    }

    @Serializable
    data class Wallpaper(val projectId: Int, val roomId: Int) : Route

    @Serializable
    data class Paint(val projectId: Int, val roomId: Int) : Route

    @Serializable
    data class Laminate(val projectId: Int, val roomId: Int) : Route

    @Serializable
    data class Documents(val projectId: Int) : Route

    @Serializable
    data class Gallery(val projectId: Int, val roomId: Int) : Route
}
