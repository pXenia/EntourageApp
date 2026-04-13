package com.entourageapp.features.rooms.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomPlanScreen
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomScreen
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomVM
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateRoomNavigation(
    projectId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val roomBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.CreateRoom.CreateForm::class, Route.CreateRoom.CreateForm.serializer())
                    subclass(Route.CreateRoom.CreatePlan::class, Route.CreateRoom.CreatePlan.serializer()
                    )
                }
            }
        },
        Route.CreateRoom.CreateForm(projectId)
    )

    val createRoomVM = koinViewModel<CreateRoomVM>()

    NavDisplay(
        backStack = roomBackStack,
        modifier = modifier,
        onBack = {
            if (roomBackStack.size <= 1) onBack()
            else roomBackStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.CreateRoom.CreateForm> { route ->
                CreateRoomScreen(
                    projectId = route.projectId,
                    viewModel = createRoomVM,
                    onDrawPlanClick = {
                        roomBackStack.add(Route.CreateRoom.CreatePlan(route.projectId))
                    },
                    onBackClick = onBack
                )
            }
            entry<Route.CreateRoom.CreatePlan> {
                CreateRoomPlanScreen(
                    viewModel = createRoomVM,
                    onBackClick = {
                        roomBackStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}