package com.tiago.kmpauthflows.presentation.login

import com.tiago.kmpauthflows.platform.PlatformActivity
import org.jetbrains.compose.resources.StringResource

enum class LoginLoadingTarget { EMAIL, GOOGLE, APPLE }

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val emailError: StringResource? = null,
    val passwordError: StringResource? = null,
    val loadingTarget: LoginLoadingTarget? = null // null = nada cargando
)

sealed interface LoginEvent {
    data class OnEmailChanged(val value: String) : LoginEvent
    data class OnPasswordChanged(val value: String) : LoginEvent
    data object OnTogglePasswordVisibility : LoginEvent
    data object OnLoginClicked : LoginEvent
    data class OnGoogleSignInClicked(val activity: PlatformActivity) : LoginEvent
    data class OnAppleSignInClicked(val activity: PlatformActivity) : LoginEvent
    data object OnGoToRegisterClicked : LoginEvent
}

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data object NavigateToRegister : LoginEffect
    data class ShowError(val messageRes: StringResource) : LoginEffect
}