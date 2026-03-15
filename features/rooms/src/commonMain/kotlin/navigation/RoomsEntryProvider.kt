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
    var createRoomVM: CreateRoomVM? = null

    entry<Route.RoomList> {
        createRoomVM = null
        RoomListScreen(
            projectId = it.projectId,
            onAddRoomClick = { navigator.navigate(Route.CreateRoom(it)) },
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.CreateRoom> {
        val vm = koinViewModel<CreateRoomVM>().also { createRoomVM = it }
        CreateRoomScreen(
            projectId = it.projectId,
            viewModel = vm,
            onDrawPlanClick = { navigator.navigate(Route.CreateRoomPlan(it)) },
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Route.CreateRoomPlan> {
        val vm = createRoomVM ?: koinViewModel<CreateRoomVM>()
        CreateRoomPlanScreen(
            viewModel = vm,
            onBackClick = {
                val planState = vm.planState.value
                vm.handleIntent(
                    CreateRoomIntent.OnPlanSaved(
                        points = planState.points,
                        walls = planState.walls,
                        square = planState.square
                    )
                )
                navigator.goBack()
            }
        )
    }
}