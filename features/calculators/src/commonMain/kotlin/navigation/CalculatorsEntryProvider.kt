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
            onWallpaperClick = { projectId, roomId -> navigator.navigate(Route.Wallpaper(projectId, roomId)) }
        )
    }
    entry<Route.Wallpaper>{
        Wallpaper(
            projectId = it.projectId,
            roomId = it.roomId,
            onBackClick = { navigator.goBack() }
        )
    }
}