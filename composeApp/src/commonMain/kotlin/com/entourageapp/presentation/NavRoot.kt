package com.entourageapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.core.navigation.rememberNavigationState
import com.entourageapp.core.navigation.toEntries
import com.entourageapp.core.ui.appBackground
import com.entourageapp.features.auth.navigation.authEntryBuilder
import com.entourageapp.features.auth.presentation.AuthState
import com.entourageapp.features.auth.presentation.AuthVM
import com.entourageapp.features.calculators.navigation.calculatorsListEntryBuilder
import com.entourageapp.features.estimates.navigation.estimatesEntryBuilder
import com.entourageapp.features.projects.navigation.projectsEntryBuilder
import com.entourageapp.features.rooms.navigation.roomsEntryBuilder
import com.entourageapp.features.userprofile.navigation.userProfileEntryBuilder
import com.entourageapp.navigation.CustomBottomBar
import com.entourageapp.navigation.TopLevelNavigationItem
import com.entourageapp.navigation.topLevelNavItems
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavRoot(
    modifier: Modifier = Modifier,
    authVM: AuthVM = koinViewModel()
) {
    val authState by authVM.state.collectAsStateWithLifecycle()

    when (authState) {
        AuthState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }
        else -> Unit
    }

    val startRoute = when (authState) {
        AuthState.Authenticated -> Route.ProjectList
        else -> Route.Login
    }

    val navigationState = rememberNavigationState(
        startRoute = startRoute,
        topLevelRoutes = topLevelNavItems.keys as Set<NavKey>
    )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated -> {
                if (navigationState.currentRoute is Route.Login) {
                    navigator.navigate(Route.ProjectList)
                }
            }
            AuthState.NotAuthenticated -> {
                if (navigationState.currentRoute !is Route.Login) {
                    navigator.navigate(Route.Login)
                }
            }
            AuthState.Loading -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .appBackground()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        NavDisplay(
            modifier = modifier.fillMaxSize(),
            onBack = navigator::goBack,
            entries = navigationState.toEntries(
                entryProvider {
                    authEntryBuilder(navigator)
                    projectsEntryBuilder(navigator)
                    calculatorsListEntryBuilder(navigator)
                    userProfileEntryBuilder(navigator)
                    estimatesEntryBuilder(navigator)
                    roomsEntryBuilder(navigator)
                }
            )
        )

        if (navigationState.shouldShowBottomBar) {
            CustomBottomBar(
                modifier = modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .align(Alignment.BottomCenter),
                items = topLevelNavItems as Map<Route, TopLevelNavigationItem>,
                navigationState = navigationState,
                navigator = navigator
            )
        }
    }
}