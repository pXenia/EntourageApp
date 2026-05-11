package com.entourageapp.features.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val generalError: String? = null,
    val showForgotPasswordDialog: Boolean = false
)

sealed class LoginIntent {
    data class OnEmailChanged(val value: String) : LoginIntent()
    data class OnPasswordChanged(val value: String) : LoginIntent()
    data class OnLoginClicked(val onSuccess: () -> Unit) : LoginIntent()
    object OnForgotPasswordClicked : LoginIntent()
    object OnDismissForgotPasswordDialog : LoginIntent()
}
