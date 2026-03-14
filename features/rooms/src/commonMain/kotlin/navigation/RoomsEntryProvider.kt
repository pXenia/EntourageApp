package com.entourageapp.features.rooms.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.rooms.presentation.RoomListScreen
import com.entourageapp.features.rooms.presentation.createplan.CreateRoomPlanScreen


fun EntryProviderScope<NavKey>.roomsEntryBuilder(navigator: Navigator) {
    entry<Route.RoomList> {
        RoomListScreen(
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.RoomInfo> {
        CreateRoomPlanScreen()
    }
}