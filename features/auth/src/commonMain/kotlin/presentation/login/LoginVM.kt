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
            is LoginIntent.OnEmailChanged -> updateEmail(intent.value)
            is LoginIntent.OnPasswordChanged -> updatePassword(intent.value)
            is LoginIntent.OnLoginClicked -> login(intent.onSuccess)
        }
    }

    private fun updateEmail(value: String) {
        _state.update { it.copy(email = value) }
    }

    private fun updatePassword(value: String) {
        _state.update { it.copy(password = value) }
    }

    private fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            authRepository.login(_state.value.email, _state.value.password)
                .onSuccess {
                    onSuccess()
                }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message) }
                }

            _state.update { it.copy(isLoading = false) }
        }
    }
}