package com.entourageapp.features.gallery.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.gallery.presentation.GalleryScreen

fun EntryProviderScope<NavKey>.galleryEntryBuilder(navigator: Navigator) {
    entry<Route.Gallery> {
        GalleryScreen(
            projectId = it.projectId,
            roomId = it.roomId,
            onBackClick = { navigator.goBack() }
        )
    }
}