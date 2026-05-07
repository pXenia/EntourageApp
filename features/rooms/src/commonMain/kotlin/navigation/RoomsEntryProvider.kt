package com.entourageapp.features.rooms.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.rooms.presentation.roomInfo.RoomInfoScreen
import com.entourageapp.features.rooms.presentation.roomdetil.RoomDetailScreen
import com.entourageapp.features.rooms.presentation.roomlist.RoomListScreen
import com.entourageapp.features.rooms.presentation.stages.StageScreen


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
            roomId = route.roomId,
            onBack = { navigator.goBack() }
        )
    }

    entry<Route.RoomInfo> { route ->
        RoomDetailScreen(
            projectId = route.projectId,
            roomId = route.roomId,
            onBackClick = { navigator.goBack() },
            onEstimateClick = { projectId, roomId -> navigator.navigate(Route.CreateEstimatePosition(projectId, roomId))},
            onGalleryClick = { projectId, roomId -> navigator.navigate(Route.Gallery(projectId, roomId))},
            onRoomInfoClick = { projectId, roomId -> navigator.navigate(Route.RoomParameters(projectId, roomId)) },
            onStagesClick = { roomId -> navigator.navigate(Route.StageList(roomId)) }
        )
    }

    entry<Route.StageList> { route ->
        StageScreen(
            roomId = route.roomId,
            onBackClick = { navigator.goBack() }
        )
    }

    entry<Route.RoomParameters> { route ->
        RoomInfoScreen(
            projectId = route.projectId,
            roomId = route.roomId,
            onBackClick = { navigator.goBack() },
            onEditClick = { projectId, roomId -> navigator.navigate(Route.RoomGraph(projectId, roomId)) }
        )
    }
}
