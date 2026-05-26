package com.entourageapp.features.projects.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.projects.presentation.createproject.CreateProjectScreen
import com.entourageapp.features.projects.presentation.projectdetail.ProjectDetailScreen
import com.entourageapp.features.projects.presentation.projectinfo.ProjectInfoScreen
import com.entourageapp.features.projects.presentation.projectlist.ProjectListScreen
import com.entourageapp.features.projects.presentation.statistics.StatisticsScreen

fun EntryProviderScope<NavKey>.projectsEntryBuilder(navigator: Navigator) {
    entry<Route.ProjectList> {
        ProjectListScreen(
            onCardClick = { navigator.navigate(Route.ProjectDetail(it)) },
            onAddProjectClick = { navigator.navigate(Route.CreateProject()) },
        )
    }

    entry<Route.ProjectDetail> { route ->
        ProjectDetailScreen(
            projectId = route.projectId,
            onBackClick = { navigator.goBack() },
            onEstimateClick = { projectId, roleId ->
                navigator.navigate(
                    Route.EstimateList(
                        projectId,
                        0,
                        roleId
                    )
                )
            },
            onGalleryClick = { projectId, roleId ->
                navigator.navigate(
                    Route.Gallery(
                        projectId,
                        0,
                        roleId
                    )
                )
            },
            onDocumentsClick = { projectId, roleId ->
                navigator.navigate(
                    Route.Documents(
                        projectId,
                        roleId
                    )
                )
            },
            onRoomListClick = { projectId, roleId ->
                navigator.navigate(
                    Route.RoomList(
                        projectId,
                        roleId
                    )
                )
            },
            onProjectStatsClick = { navigator.navigate(Route.ProjectsStats(it)) },
            onProjectInfoClick = { projectId, roleId ->
                navigator.navigate(
                    Route.ProjectInfo(
                        projectId,
                        roleId
                    )
                )
            }
        )
    }

    entry<Route.CreateProject> { route ->
        CreateProjectScreen(
            projectId = route.projectId,
            onBackClick = { navigator.goBack() }
        )
    }

    entry<Route.ProjectsStats> { route ->
        StatisticsScreen(
            onBackClick = { navigator.goBack() },
            projectId = route.projectId
        )
    }

    entry<Route.ProjectInfo> { route ->
        ProjectInfoScreen(
            projectId = route.projectId,
            onBackClick = { navigator.goBack() },
            onEditClick = { id -> navigator.navigate(Route.CreateProject(id)) }
        )
    }
}