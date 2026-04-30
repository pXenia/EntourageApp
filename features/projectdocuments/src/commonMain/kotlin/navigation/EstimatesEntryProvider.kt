package com.entourageapp.features.projectdocuments.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.projectdocuments.presentation.DocumentListScreen

fun EntryProviderScope<NavKey>.documentsEntryBuilder(navigator: Navigator) {
    entry<Route.Documents> {
        DocumentListScreen(
            projectId = it.projectId,
            onBackClick = { navigator.goBack() }
        )
    }
}