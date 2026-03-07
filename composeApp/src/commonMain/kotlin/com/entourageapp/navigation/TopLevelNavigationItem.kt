package com.entourageapp.navigation

import com.entourageapp.core.navigation.Route
import com.entourageapp.core.ui.calculator
import com.entourageapp.core.ui.folder
import com.entourageapp.core.ui.user
import org.jetbrains.compose.resources.DrawableResource


data class TopLevelNavigationItem(
    val icon: DrawableResource,
    val title: String
)

val projectsListFeature = TopLevelNavigationItem(
    icon = folder,
    title = "Проекты",
)

val calculatorsListFeature = TopLevelNavigationItem(
    icon = calculator,
    title = "Калькуляторы",
)

val userProfileFeature = TopLevelNavigationItem(
    icon = user,
    title = "Профиль",
)

val topLevelNavItems = mapOf(
    Route.ProjectList to projectsListFeature,
    Route.RoomInfo to calculatorsListFeature,
    Route.UserProfile to userProfileFeature
)
