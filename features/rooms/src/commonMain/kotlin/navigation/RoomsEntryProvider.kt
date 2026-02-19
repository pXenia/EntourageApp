package navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import presentation.RoomListScreen


fun EntryProviderScope<NavKey>.roomsEntryBuilder(navigator: Navigator) {
    entry<Route.RoomList> {
        RoomListScreen(
            onBackClick = { navigator.goBack() }
        )
    }
}