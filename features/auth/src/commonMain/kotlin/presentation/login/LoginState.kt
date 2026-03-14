package presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val generalError: String? = null
)

sealed class LoginIntent {
    data class OnEmailChanged(val value: String) : LoginIntent()
    data class OnPasswordChanged(val value: String) : LoginIntent()
    data class OnLoginClicked(val onSuccess: () -> Unit) : LoginIntent()
}