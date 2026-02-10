package com.example.core.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(val state: NavigationState){
    fun navigate(route: NavKey){
        if (route in state.backStacks.keys){
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack(){
        val currentStack = state.backStacks[state.topLevelRoute] ?:
        error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute){
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
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
        }
    }

    fun getCurrentStackSize(): Int {
        return state.backStacks[state.topLevelRoute]?.size ?: 0
    }
}