package com.entourageapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.network.TokenStore
import domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthVM(
    private val tokenStore: TokenStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated

    init {
        viewModelScope.launch {
            val hasToken = tokenStore.hasAccessToken.first()
            if (!hasToken) {
                _isAuthenticated.value = false
                return@launch
            }
            authRepository.getMe()
                .onSuccess { _isAuthenticated.value = true }
                .onFailure {
                    authRepository.refreshTokens()
                        .onSuccess { _isAuthenticated.value = true }
                        .onFailure {
                            tokenStore.clear()
                            _isAuthenticated.value = false
                        }
                }
        }

        viewModelScope.launch {
            tokenStore.hasAccessToken.collect { has ->
                if (!has) _isAuthenticated.value = false
            }
        }
    }
}