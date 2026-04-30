package com.entourageapp.features.auth.presentation.register

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null
)

sealed class RegisterIntent {
    data class OnNameChanged(val value: String) : RegisterIntent()
    data class OnEmailChanged(val value: String) : RegisterIntent()
    data class OnPasswordChanged(val value: String) : RegisterIntent()
    data class OnConfirmPasswordChanged(val value: String) : RegisterIntent()
    data class OnRegisterClicked(val onSuccess: () -> Unit) : RegisterIntent()
}