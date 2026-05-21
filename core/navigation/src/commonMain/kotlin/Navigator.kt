package com.entourageapp.core.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(val state: NavigationState) {
    
    fun navigate(route: NavKey) {
        val exactRoot = state.backStacks.keys.find { it == route }
        
        if (exactRoot != null) {
            state.topLevelRoute = exactRoot
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute] ?: return
        
        if (currentStack.size > 1) {
            currentStack.removeLastOrNull()
        } else {
            if (state.topLevelRoute != state.startRoute) {
                state.topLevelRoute = state.startRoute
            }
        }
    }

    fun popUpTo(route: NavKey, inclusive: Boolean = false) {
        val currentStack = state.backStacks[state.topLevelRoute] ?: return
        val index = currentStack.indexOf(route)

        if (index != -1) {
            val targetSize = if (inclusive) index else index + 1
            while (currentStack.size > targetSize) {
                currentStack.removeLastOrNull()
            }
        } else {
            currentStack.clear()
            currentStack.add(route)
        }
    }

    fun getCurrentStackSize(): Int {
        return state.backStacks[state.topLevelRoute]?.size ?: 0
    }
}
