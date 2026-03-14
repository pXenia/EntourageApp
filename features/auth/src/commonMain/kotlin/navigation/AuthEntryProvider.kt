package navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import presentation.login.LoginScreen
import presentation.register.RegisterScreen

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
            onBackClick = { navigator.goBack() },
            onNavigateToLogin = { navigator.navigate(Route.Login) },
            onRegistrationSuccess = { navigator.navigateToMain() }
        )
    }
}

fun Navigator.navigateToMain() {
    navigate(Route.ProjectList)
}