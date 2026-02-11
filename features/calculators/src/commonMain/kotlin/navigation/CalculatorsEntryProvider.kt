package com.entourageapp.features.calculators.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.entourageapp.core.navigation.Navigator
import com.entourageapp.core.navigation.Route
import com.entourageapp.features.calculators.presentation.CalculatorListScreen

fun EntryProviderScope<NavKey>.calculatorsListEntryBuilder(navigator: Navigator) {
    entry<Route.CalculatorsList> {
        CalculatorListScreen()
    }
}