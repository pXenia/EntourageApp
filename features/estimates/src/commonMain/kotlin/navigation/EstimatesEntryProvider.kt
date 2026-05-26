package com.entourageapp.features.estimates.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.estimates.presentation.createestimateposition.CreatePositionScreen
import com.entourageapp.features.estimates.presentation.estimatelist.EstimateListScreen

fun EntryProviderScope<NavKey>.estimatesEntryBuilder(navigator: Navigator) {
    entry<Route.EstimateList> {
        EstimateListScreen(
            projectId = it.projectId,
            roomId = it.roomId,
            roleId = it.roleId,
            onAddPosition = { projectId, roomId -> navigator.navigate(Route.CreateEstimatePosition(projectId, roomId)) },
            onEditPosition = { projectId, roomId, itemId -> navigator.navigate(Route.CreateEstimatePosition(projectId, roomId, itemId)) },
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.CreateEstimatePosition> {
        CreatePositionScreen(
            projectId =  it.projectId,
            roomId = it.roomId,
            itemId = it.itemId,
            onBackClick = { navigator.goBack() },
            onCalculateClick = { projectId, roomId -> navigator.navigate(Route.CalculatorsListFromEstimate(projectId, roomId))}
        )
    }
}