package com.entourageapp.features.calculators.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.calculators.presentation.CalculatorListScreen
import com.entourageapp.features.calculators.presentation.wallpaper.Wallpaper

fun EntryProviderScope<NavKey>.calculatorsListEntryBuilder(navigator: Navigator) {
    entry<Route.CalculatorsList> {
        CalculatorListScreen(
            projectId = it.projectId,
            roomId = it.roomId,
            onWallpaperClick = { projectId, roomId -> navigator.navigate(Route.Wallpaper(projectId, roomId)) }
        )
    }
    entry<Route.Wallpaper>{
        Wallpaper(
            projectId = it.projectId,
            roomId = it.roomId,
            onBackClick = { navigator.goBack() },
            transferToEstimate = { navigator.goBackWithResult(it) }
        )
    }
}

private fun Navigator.goBackWithResult(amount: Int) {
    val currentStack = state.backStacks[state.topLevelRoute] ?: return

    val index = currentStack.indexOfLast { it is Route.CreateEstimatePosition }
    if (index != -1) {
        val current = currentStack[index] as Route.CreateEstimatePosition
        while (currentStack.size > index + 1) {
            currentStack.removeLastOrNull()
        }
        currentStack[index] = current.copy(amount = amount)
    }
}