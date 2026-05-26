package com.entourageapp.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.auth.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginVM(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnEmailChanged -> _state.update { it.copy(email = intent.value, generalError = null) }
            is LoginIntent.OnPasswordChanged -> _state.update { it.copy(password = intent.value, generalError = null) }
            is LoginIntent.OnLoginClicked -> login(intent.onSuccess)
            LoginIntent.OnForgotPasswordClicked -> _state.update { it.copy(showForgotPasswordDialog = true) }
            LoginIntent.OnDismissForgotPasswordDialog -> _state.update { it.copy(showForgotPasswordDialog = false) }
        }
    }

    private fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authRepository.login(state.value.email, state.value.password)
                .onSuccess { onSuccess() }
                .onFailure { e -> _state.update { it.copy(generalError = e.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }
}