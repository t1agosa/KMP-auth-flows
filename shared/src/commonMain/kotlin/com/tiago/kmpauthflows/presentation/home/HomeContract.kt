package com.tiago.kmpauthflows.presentation.home

import com.tiago.kmpauthflows.domain.model.User
import org.jetbrains.compose.resources.StringResource

data class HomeState(
    val user: User? = null,
    val isCheckingAuthState: Boolean = true, // true mientras no sabemos si hay sesión o no
    val isLoggingOut: Boolean = false
)

sealed interface HomeEvent {
    data object OnLogoutClicked : HomeEvent
}

sealed interface HomeEffect {
    data object NavigateToLogin : HomeEffect
    data class ShowError(val messageRes: StringResource) : HomeEffect
}