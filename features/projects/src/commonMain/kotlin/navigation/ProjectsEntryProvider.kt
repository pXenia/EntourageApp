package com.entourageapp.features.projects.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.projects.presentation.ProjectDetailScreen
import com.entourageapp.features.projects.presentation.createproject.CreateProjectScreen
import com.entourageapp.features.projects.presentation.projectlist.ProjectListScreen

fun EntryProviderScope<NavKey>.projectsEntryBuilder(navigator: Navigator) {
    entry<Route.ProjectList> {
        ProjectListScreen(
            onCardClick = { navigator.navigate(Route.ProjectDetail) },
            onAddProjectClick = { navigator.navigate(Route.CreateProject) }
        )
    }

    entry<Route.ProjectDetail> {
        ProjectDetailScreen(
            onBackClick = { navigator.goBack() },
            onEstimateClick = { navigator.navigate(Route.EstimateList) },
            onRoomListClick = { navigator.navigate(Route.RoomList) },
            onProjectInfoClick = { navigator.navigate(Route.RoomInfo) }
        )
    }

    entry<Route.CreateProject> {
        CreateProjectScreen(
            onBackClick = { navigator.goBack() }
        )
    }
}