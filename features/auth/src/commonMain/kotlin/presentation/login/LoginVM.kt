package presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.AuthRepository
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