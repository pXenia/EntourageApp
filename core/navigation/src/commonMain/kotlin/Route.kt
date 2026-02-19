package com.entourageapp.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object ProjectList: Route
    @Serializable
    data object CalculatorsList: Route
    @Serializable
    data object UserProfile: Route
    @Serializable
    data object ProjectDetail: Route
    @Serializable
    data object CreateProject: Route
    @Serializable
    data object EstimateList: Route
    @Serializable
    data object RoomList: Route
}
