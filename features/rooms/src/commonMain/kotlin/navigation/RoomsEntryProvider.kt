package com.entourageapp.features.rooms.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.rooms.presentation.createplan.CreateRoomPlanScreen
import com.entourageapp.features.rooms.presentation.roomlist.RoomListScreen


fun EntryProviderScope<NavKey>.roomsEntryBuilder(navigator: Navigator) {
    entry<Route.RoomList> {
        RoomListScreen(
            projectId = it.projectId,
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.RoomInfo> {
        CreateRoomPlanScreen()
    }
}