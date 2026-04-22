package com.entourageapp.features.rooms.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.rooms.presentation.roomdetil.RoomDetailScreen
import com.entourageapp.features.rooms.presentation.roomlist.RoomListScreen


fun EntryProviderScope<NavKey>.roomsEntryBuilder(navigator: Navigator) {
    entry<Route.RoomList> {
        RoomListScreen(
            projectId = it.projectId,
            onAddRoomClick = { navigator.navigate(Route.RoomGraph(it)) },
            onRoomClick = { projectId, roomId -> navigator.navigate(Route.RoomInfo(projectId,roomId)) },
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.RoomGraph> { route ->
        CreateRoomNavigation(
            projectId = route.projectId,
            onBack = { navigator.goBack() }
        )
    }

    entry<Route.RoomInfo> { route ->
        RoomDetailScreen(
            projectId = route.projectId,
            roomId = route.roomId,
            onBackClick = { navigator.goBack() },
            onEstimateClick = { projectId, roomId -> navigator.navigate(Route.CreateEstimatePosition(projectId, roomId))}
        )
    }
}