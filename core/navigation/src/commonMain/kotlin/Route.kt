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
}