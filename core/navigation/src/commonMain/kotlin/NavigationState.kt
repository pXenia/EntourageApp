package com.entourageapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


class NavigationState(
    val startRoute: NavKey, // главный экран приложения
    topLevelRoute: MutableState<NavKey>, // текущая активная вкладка
    val backStacks: Map<NavKey, NavBackStack<NavKey>> // ключ - стек (несколько стеков одновременно)
) {
    var topLevelRoute: NavKey by topLevelRoute
    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }

    val currentRoute: NavKey
        get() = backStacks[topLevelRoute]?.lastOrNull() ?: topLevelRoute

    private val authRoutes = setOf(Route.AuthStart, Route.Login, Route.Registration)
    private val topLevelRoutes = setOf(Route.ProjectList, Route.CalculatorsList(0,0), Route.UserProfile)

    val shouldShowBottomBar: Boolean
        get() = (currentRoute !in authRoutes) && (currentRoute in topLevelRoutes)
}


@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>
): NavigationState {

    val topLevelRoute = rememberSerializable(
        startRoute,
        topLevelRoutes,
        configuration = config,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class))
    ) {
        mutableStateOf(startRoute)
    }

    val authRoutes = setOf(Route.AuthStart, Route.Login, Route.Registration)

    val backStacks = (topLevelRoutes + authRoutes).associateWith { key ->
        rememberNavBackStack(
            configuration = config,
            key
        )
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks
        )
    }
}

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.AuthGraph::class, Route.AuthGraph.serializer())
            subclass(Route.AuthStart::class, Route.AuthStart.serializer())
            subclass(Route.Login::class, Route.Login.serializer())
            subclass(Route.Registration::class, Route.Registration.serializer())
            subclass(Route.ProjectList::class, Route.ProjectList.serializer())
            subclass(Route.ProjectDetail::class, Route.ProjectDetail.serializer())
            subclass(Route.CreateProject::class, Route.CreateProject.serializer())
            subclass(Route.CalculatorsList::class, Route.CalculatorsList.serializer())
            subclass(Route.UserProfile::class, Route.UserProfile.serializer())
            subclass(Route.RoomInfo::class, Route.RoomInfo.serializer())
            subclass(Route.RoomGraph::class, Route.RoomGraph.serializer())
            subclass(Route.RoomList::class, Route.RoomList.serializer())
            subclass(Route.CreateEstimatePosition::class, Route.CreateEstimatePosition.serializer())
            subclass(Route.EstimateList::class, Route.EstimateList.serializer())
            subclass(Route.Wallpaper::class, Route.Wallpaper.serializer())
            subclass(Route.Paint::class, Route.Paint.serializer())
            subclass(Route.Laminate::class, Route.Laminate.serializer())
            subclass(Route.Documents::class, Route.Documents.serializer())
            subclass(Route.Gallery::class, Route.Gallery.serializer())
            subclass(Route.ManageProjects::class, Route.ManageProjects.serializer())
            subclass(Route.ProjectsStats::class, Route.ProjectsStats.serializer())
            subclass(Route.ProjectInfo::class, Route.ProjectInfo.serializer())
        }
    }
}

@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {

    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator()
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}