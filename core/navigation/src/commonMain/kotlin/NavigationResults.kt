package com.entourageapp.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object NavigationResults {
    private val _results = MutableSharedFlow<NavigationResult>(extraBufferCapacity = 1)
    val results: SharedFlow<NavigationResult> = _results

    fun send(result: NavigationResult) {
        _results.tryEmit(result)
    }
}

sealed interface NavigationResult {
    data class CalculatorResult(val amount: Int) : NavigationResult
}