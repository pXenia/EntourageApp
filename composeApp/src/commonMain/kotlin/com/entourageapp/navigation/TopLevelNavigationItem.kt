package com.entourageapp.navigation

import com.entourageapp.core.navigation.Route
import entourageapp.composeapp.generated.resources.Res
import entourageapp.composeapp.generated.resources.calculator
import entourageapp.composeapp.generated.resources.folder
import entourageapp.composeapp.generated.resources.user
import org.jetbrains.compose.resources.DrawableResource


data class TopLevelNavigationItem(
    val icon: DrawableResource,
    val title: String
)

val projectsListFeature = TopLevelNavigationItem(
    icon = Res.drawable.folder,
    title = "Проекты",
)

val calculatorsListFeature = TopLevelNavigationItem(
    icon = Res.drawable.calculator,
    title = "Калькуляторы",
)

val userProfileFeature = TopLevelNavigationItem(
    icon = Res.drawable.user,
    title = "Профиль",
)

val topLevelNavItems = mapOf(
    Route.ProjectList to projectsListFeature,
    Route.CalculatorsList to calculatorsListFeature,
    Route.UserProfile to userProfileFeature
)
