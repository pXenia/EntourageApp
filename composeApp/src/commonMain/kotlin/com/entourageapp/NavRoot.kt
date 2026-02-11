package com.entourageapp

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.core.navigation.rememberNavigationState
import com.entourageapp.core.navigation.toEntries
import com.entourageapp.features.calculators.navigation.calculatorsListEntryBuilder
import com.entourageapp.features.projects.navigation.projectListEntryBuilder
import com.entourageapp.features.userprofile.navigation.userProfileEntryBuilder
import com.entourageapp.navigation.NavBottomBar
import com.entourageapp.navigation.topLevelNavItems


@Composable
fun NavRoot(
    modifier: Modifier = Modifier
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.ProjectList,
        topLevelRoutes = topLevelNavItems.keys as Set<NavKey>
    )
    val navigator = remember {
        Navigator(navigationState)
    }

    Scaffold(
        bottomBar = {
            if (navigationState.shouldShowBottomBar) {
                NavBottomBar(
                    selectedKey = navigationState.topLevelRoute,
                    onSelectKey = {
                        navigator.navigate(it)
                    }
                )
            }
        }
    ) { padding ->
        NavDisplay(
            modifier = modifier.fillMaxSize().padding(padding),
            onBack = navigator::goBack,
            entries = navigationState.toEntries(
                entryProvider {
                    projectListEntryBuilder(navigator)
                    calculatorsListEntryBuilder(navigator)
                    userProfileEntryBuilder(navigator)
                }
            ),
            transitionSpec = { NavigationAnimations.defaultTransitionSpec() },
            popTransitionSpec = { NavigationAnimations.defaultPopTransitionSpec() },
            predictivePopTransitionSpec = { NavigationAnimations.defaultPopTransitionSpec() }
        )
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
