package presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterVM(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.OnNameChanged -> _state.update { it.copy(name = intent.value) }
            is RegisterIntent.OnEmailChanged -> _state.update { it.copy(email = intent.value, emailError = null) }
            is RegisterIntent.OnPasswordChanged -> _state.update { it.copy(password = intent.value, passwordError = null) }
            is RegisterIntent.OnConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = intent.value, confirmPasswordError = null) }
            is RegisterIntent.OnRegisterClicked -> validateAndRegister(intent.onSuccess)
        }
    }

    private fun validateAndRegister(onSuccess: () -> Unit) {
        val s = _state.value
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        val nameErr = if (s.name.isBlank()) "Введите имя" else null
        val emailErr = if (!s.email.matches(emailPattern.toRegex())) "Введите корректный адрес электронной почты" else null
        val passErr = if (s.password.length < 6) "Используйте не менее 6 символов" else null
        val confirmErr = if (s.confirmPassword != s.password) "Пароли не совпадают" else null

        if (emailErr != null || passErr != null || confirmErr != null) {
            _state.update { it.copy(
                nameError = nameErr,
                emailError = emailErr,
                passwordError = passErr,
                confirmPasswordError = confirmErr
            ) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authRepository.register(s.name, s.email, s.password)
                .onSuccess { onSuccess() }
                .onFailure { e -> _state.update { it.copy(generalError = e.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }
}