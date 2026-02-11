package com.entourageapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.vector.ImageVector
import com.entourageapp.core.navigation.Route


data class TopLevelNavigationItem(
    val icon: ImageVector,
    val title: String
)

val projectsListFeature = TopLevelNavigationItem(
    icon = Icons.Default.AccountCircle,
    title = "Задачи",
)

val calculatorsListFeature = TopLevelNavigationItem(
    icon = Icons.Default.Add,
    title = "Цвета",
)

val userProfileFeature = TopLevelNavigationItem(
    icon = Icons.Default.Add,
    title = "Добавить",
)

val topLevelNavItems = mapOf(
    Route.ProjectList to projectsListFeature,
    Route.CalculatorsList to calculatorsListFeature,
    Route.UserProfile to userProfileFeature
)
