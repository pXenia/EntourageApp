package com.entourageapp.features.calculators.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.NavigationResult
import com.entourageapp.core.navigation.NavigationResults
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.calculators.presentation.CalculatorListScreen
import com.entourageapp.features.calculators.presentation.laminate.Laminate
import com.entourageapp.features.calculators.presentation.paint.Paint
import com.entourageapp.features.calculators.presentation.wallpaper.Wallpaper

fun EntryProviderScope<NavKey>.calculatorsListEntryBuilder(navigator: Navigator) {
    entry<Route.CalculatorsList> {
        CalculatorListScreen(
            projectId = it.projectId,
            roomId = it.roomId,
            onWallpaperClick = { projectId, roomId -> navigator.navigate(Route.Wallpaper(projectId, roomId)) },
            onPaintClick = { projectId, roomId -> navigator.navigate(Route.Paint(projectId, roomId)) },
            onLaminateClick = { projectId, roomId -> navigator.navigate(Route.Laminate(projectId, roomId)) }
        )
    }
    entry<Route.Wallpaper>{
        Wallpaper(
            projectId = it.projectId,
            roomId = it.roomId,
            onBackClick = { navigator.goBack() },
            transferToEstimate = {
                navigator.goBackWithResult()
                NavigationResults.send(NavigationResult.CalculatorResult(it))
            }
        )
    }

    entry<Route.Paint>{
        Paint(
            projectId = it.projectId,
            roomId = it.roomId,
            onBackClick = { navigator.goBack() },
            transferToEstimate = {
                navigator.goBackWithResult()
                NavigationResults.send(NavigationResult.CalculatorResult(it.toInt()))
            }
        )
    }

    entry<Route.Laminate>{
        Laminate(
            projectId = it.projectId,
            roomId = it.roomId,
            onBackClick = { navigator.goBack() },
            transferToEstimate = {
                navigator.goBackWithResult()
                NavigationResults.send(NavigationResult.CalculatorResult(it.toInt()))
            }
        )
    }
}

private fun Navigator.goBackWithResult() {
    val currentStack = state.backStacks[state.topLevelRoute] ?: return
    val index = currentStack.indexOfLast { it is Route.CreateEstimatePosition }
    if (index != -1) {
        while (currentStack.size > index + 1) {
            currentStack.removeLastOrNull()
        }
    }
}