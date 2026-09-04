package com.tiago.kmpauthflows.presentation.register

import org.jetbrains.compose.resources.StringResource

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val emailError: StringResource? = null,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
    val isLoading: Boolean = false
)

sealed interface RegisterEvent {
    data class OnEmailChanged(val value: String) : RegisterEvent
    data class OnPasswordChanged(val value: String) : RegisterEvent
    data class OnConfirmPasswordChanged(val value: String) : RegisterEvent
    data object OnTogglePasswordVisibility : RegisterEvent
    data object OnToggleConfirmPasswordVisibility : RegisterEvent
    data object OnRegisterClicked : RegisterEvent
    data object OnGoToLoginClicked : RegisterEvent
}

sealed interface RegisterEffect {
    data object NavigateToHome : RegisterEffect
    data object NavigateToLogin : RegisterEffect
    data class ShowError(val messageRes: StringResource) : RegisterEffect
}