package com.entourageapp.features.rooms.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomIntent
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomPlanScreen
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomScreen
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomVM
import com.entourageapp.features.rooms.presentation.roomlist.RoomListScreen
import org.koin.compose.viewmodel.koinViewModel


fun EntryProviderScope<NavKey>.roomsEntryBuilder(navigator: Navigator) {
    entry<Route.RoomList> {
        RoomListScreen(
            projectId = it.projectId,
            onAddRoomClick = { navigator.navigate(Route.RoomGraph(it))
            },
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.RoomGraph> { route ->
        CreateRoomNavigation(
            projectId = route.projectId,
            onBack = { navigator.goBack() }
        )
    }
}