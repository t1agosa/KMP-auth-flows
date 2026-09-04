package com.tiago.kmpauthflows.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.usecase.LoginWithAppleUseCase
import com.tiago.kmpauthflows.domain.usecase.LoginWithEmailUseCase
import com.tiago.kmpauthflows.domain.usecase.LoginWithGoogleUseCase
import com.tiago.kmpauthflows.domain.usecase.ValidateEmailUseCase
import com.tiago.kmpauthflows.domain.usecase.ValidatePasswordUseCase
import com.tiago.kmpauthflows.domain.util.Result
import com.tiago.kmpauthflows.platform.PlatformActivity
import com.tiago.kmpauthflows.presentation.common.toMessageRes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithAppleUseCase: LoginWithAppleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged ->
                _state.update { it.copy(email = event.value, emailError = null) }

            is LoginEvent.OnPasswordChanged ->
                _state.update { it.copy(password = event.value, passwordError = null) }

            LoginEvent.OnTogglePasswordVisibility ->
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

            LoginEvent.OnLoginClicked -> onLoginClicked()
            is LoginEvent.OnGoogleSignInClicked -> onGoogleSignInClicked(event.activity)
            is LoginEvent.OnAppleSignInClicked -> onAppleSignInClicked(event.activity)
            LoginEvent.OnGoToRegisterClicked -> emitEffect(LoginEffect.NavigateToRegister)
        }
    }

    private fun onLoginClicked() {
        val current = _state.value

        val emailValidation = validateEmailUseCase(current.email)
        val passwordValidation = validatePasswordUseCase(current.password)

        val emailError = (emailValidation as? Result.Error)?.exception as? AuthException
        val passwordError = (passwordValidation as? Result.Error)?.exception as? AuthException

        if (emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    emailError = emailError?.toMessageRes(),
                    passwordError = passwordError?.toMessageRes()
                )
            }
            return // corta antes de llamar al repositorio — ver punto 27/31, evitar trabajo innecesario
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = loginWithEmailUseCase(current.email, current.password)
            handleResult(result)
        }
    }

    private fun onGoogleSignInClicked(activity: PlatformActivity) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = loginWithGoogleUseCase(activity)
            handleResult(result)
        }
    }

    private fun onAppleSignInClicked(activity: PlatformActivity) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = loginWithAppleUseCase(activity)
            handleResult(result)
        }
    }

    private suspend fun handleResult(result: Result<User>) {
        _state.update { it.copy(isLoading = false) }

        when (result) {
            is Result.Success -> _effect.emit(LoginEffect.NavigateToHome)
            is Result.Error -> {
                val authException = result.exception as? AuthException
                    ?: AuthException.Unknown(result.exception)

                if (authException is AuthException.SignInCancelled) {
                    return
                }

                _effect.emit(LoginEffect.ShowError(authException.toMessageRes()))
            }
        }
    }

    private fun emitEffect(effect: LoginEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}