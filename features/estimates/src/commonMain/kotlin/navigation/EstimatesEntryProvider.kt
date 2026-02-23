package navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import presentation.EstimateListScreen

fun EntryProviderScope<NavKey>.estimatesEntryBuilder(navigator: Navigator) {
    entry<Route.EstimateList> {
        EstimateListScreen()
    }
}