package navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import presentation.createestimateposition.CreatePositionScreen
import presentation.estimatelist.EstimateListScreen

fun EntryProviderScope<NavKey>.estimatesEntryBuilder(navigator: Navigator) {
    entry<Route.EstimateList> {
        EstimateListScreen(
            projectId = it.projectId,
            onAddPosition = { navigator.navigate(Route.CreateEstimatePosition(it)) }
        )
    }
    entry<Route.CreateEstimatePosition> {
        CreatePositionScreen(
            projectId =  it.projectId,
            onBackClick = { navigator.goBack() }
        )
    }
}