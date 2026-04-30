package com.entourageapp.features.projects.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.projects.presentation.createproject.CreateProjectScreen
import com.entourageapp.features.projects.presentation.projectdetail.ProjectDetailScreen
import com.entourageapp.features.projects.presentation.projectlist.ProjectListScreen

fun EntryProviderScope<NavKey>.projectsEntryBuilder(navigator: Navigator) {
    entry<Route.ProjectList> {
        ProjectListScreen(
            onCardClick = { navigator.navigate(Route.ProjectDetail(it)) },
            onAddProjectClick = { navigator.navigate(Route.CreateProject) }
        )
    }

    entry<Route.ProjectDetail> {
        ProjectDetailScreen(
            projectId = it.projectId,
            onBackClick = { navigator.goBack() },
            onEstimateClick = { navigator.navigate(Route.EstimateList(it)) },
            onGalleryClick = { navigator.navigate(Route.Gallery(it, 0)) },
            onDocumentsClick = { navigator.navigate(Route.Documents(it)) },
            onRoomListClick = { navigator.navigate(Route.RoomList(it)) },
            onProjectInfoClick = { }
        )
    }

    entry<Route.CreateProject> {
        CreateProjectScreen(
            onBackClick = { navigator.goBack() }
        )
    }
}