package com.entourageapp.features.userprofile.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.userprofile.presentation.UserProfileScreen

fun EntryProviderScope<NavKey>.userProfileEntryBuilder(navigator: Navigator) {
    entry<Route.UserProfile> {
        UserProfileScreen()
    }
}