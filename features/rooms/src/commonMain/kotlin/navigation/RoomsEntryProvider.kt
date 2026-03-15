package com.entourageapp.features.rooms.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.rooms.presentation.createplan.CreateRoomPlanScreen
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomScreen
import com.entourageapp.features.rooms.presentation.roomlist.RoomListScreen


fun EntryProviderScope<NavKey>.roomsEntryBuilder(navigator: Navigator) {
    entry<Route.RoomList> {
        RoomListScreen(
            projectId = it.projectId,
            onAddRoomClick = { navigator.navigate(Route.CreateRoom(it)) },
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.CreateRoomPlan> {
        CreateRoomPlanScreen(
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.CreateRoom> {
        CreateRoomScreen(
            projectId = it.projectId,
            onDrawPlanClick = { navigator.navigate(Route.CreateRoomPlan(it)) },
            onBackClick = { navigator.goBack() }
        )
    }
}