package com.entourageapp.presentation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
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
import com.entourageapp.features.calculators.navigation.calculatorsListEntryBuilder
import com.entourageapp.features.projects.navigation.projectsEntryBuilder
import com.entourageapp.features.rooms.navigation.roomsEntryBuilder
import com.entourageapp.features.userprofile.navigation.userProfileEntryBuilder
import com.entourageapp.navigation.CustomBottomBar
import com.entourageapp.navigation.TopLevelNavigationItem
import com.entourageapp.navigation.topLevelNavItems
import navigation.authEntryBuilder
import navigation.estimatesEntryBuilder
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun NavRoot(
    modifier: Modifier = Modifier,
    authVM: AuthVM = koinViewModel()
) {
    val isAuthenticated by authVM.isAuthenticated.collectAsStateWithLifecycle()
    if (isAuthenticated == null) return
    val startRoute = if (isAuthenticated == true) Route.ProjectList else Route.Login
    val navigationState = rememberNavigationState(
        startRoute = startRoute,
        topLevelRoutes = topLevelNavItems.keys as Set<NavKey>
    )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    LaunchedEffect(isAuthenticated) {
        when (isAuthenticated) {
            true -> navigator.navigate(Route.ProjectList)
            false -> navigator.navigate(Route.Login)
            null -> Unit
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
            ),
            transitionSpec = { NavigationAnimations.defaultTransitionSpec() },
            popTransitionSpec = { NavigationAnimations.defaultPopTransitionSpec() },
            predictivePopTransitionSpec = { NavigationAnimations.defaultPopTransitionSpec() }
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

object NavigationAnimations {
    const val DURATION_MS = 1000
    const val SLIDE_OUT = 3

    fun slideInSpec() = tween<IntOffset>(DURATION_MS)
    fun fadeSpec() = tween<Float>(DURATION_MS)

    fun defaultTransitionSpec(): ContentTransform {
        return slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = slideInSpec()
        ) togetherWith slideOutHorizontally(
            targetOffsetX = { -it / SLIDE_OUT},
            animationSpec = slideInSpec()
        ) + fadeOut(animationSpec = fadeSpec())
    }

    fun defaultPopTransitionSpec(): ContentTransform {
        return slideInHorizontally(
            initialOffsetX = { -it / SLIDE_OUT },
            animationSpec = slideInSpec()
        ) + fadeIn(animationSpec = fadeSpec()) togetherWith slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = slideInSpec()
        )
    }
}
