package com.entourageapp.features.projects.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.projects.presentation.ProjectDetailScreen
import com.entourageapp.features.projects.presentation.ProjectListScreen

fun EntryProviderScope<NavKey>.projectListEntryBuilder(navigator: Navigator) {
    entry<Route.ProjectList> {
        ProjectListScreen(
            onCardClick = {navigator.navigate(Route.ProjectDetail)}
        )
    }

    entry<Route.ProjectDetail> {
        ProjectDetailScreen()
    }
}