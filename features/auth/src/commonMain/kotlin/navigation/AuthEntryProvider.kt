package navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import presentation.LoginScreen
import presentation.RegisterScreen

fun EntryProviderScope<NavKey>.authEntryBuilder(navigator: Navigator) {
    entry<Route.Login> {
        LoginScreen(
            onLoginSuccess = { navigator.navigateToMain() },
            onBackClick = { navigator.goBack() },
            onNavigateToRegister = { navigator.navigate(Route.Registration) },
        )
    }
    entry<Route.Registration> {
        RegisterScreen(
            onBack = { navigator.goBack() },
            onRegistrationSuccess = { navigator.navigateToMain() }
        )
    }
}

fun Navigator.navigateToMain() {
    navigate(Route.ProjectList)
}