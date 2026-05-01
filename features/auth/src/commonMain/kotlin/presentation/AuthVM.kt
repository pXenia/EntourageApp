package com.entourageapp.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.network.TokenStore
import com.entourageapp.features.auth.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthVM(
    private val tokenStore: TokenStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            tokenStore.hasAccessToken.collect { hasToken ->
                if (hasToken) {
                    if (_state.value !is AuthState.Authenticated) {
                        checkAuth()
                    }
                } else {
                    _state.value = AuthState.NotAuthenticated
                }
            }
        }
    }

    private fun checkAuth() {
        viewModelScope.launch {
            // Перед проверкой убеждаемся, что токен все еще на месте
            authRepository.getMe()
                .onSuccess {
                    _state.value = AuthState.Authenticated
                }
                .onFailure {
                    authRepository.refreshTokens()
                        .onSuccess {
                            _state.value = AuthState.Authenticated
                        }
                        .onFailure {
                            tokenStore.clear()
                            _state.value = AuthState.NotAuthenticated
                        }
                }
        }
    }

    fun retry() {
        _state.value = AuthState.Loading
        checkAuth()
    }
}

sealed interface AuthState {
    data object Loading : AuthState
    data object Authenticated : AuthState
    data object NotAuthenticated : AuthState
}