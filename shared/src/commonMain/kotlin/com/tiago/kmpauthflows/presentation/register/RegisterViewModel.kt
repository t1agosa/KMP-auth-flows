package com.tiago.kmpauthflows.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.usecase.RegisterWithEmailUseCase
import com.tiago.kmpauthflows.domain.usecase.ValidateEmailUseCase
import com.tiago.kmpauthflows.domain.usecase.ValidatePasswordUseCase
import com.tiago.kmpauthflows.domain.util.Result
import com.tiago.kmpauthflows.presentation.common.toMessageRes
import com.tiago.kmpauthflows.shared.generated.resources.Res
import com.tiago.kmpauthflows.shared.generated.resources.register_error_passwords_dont_match
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val registerWithEmailUseCase: RegisterWithEmailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnEmailChanged ->
                _state.update { it.copy(email = event.value, emailError = null) }

            is RegisterEvent.OnPasswordChanged ->
                _state.update { it.copy(password = event.value, passwordError = null, confirmPasswordError = null) }

            is RegisterEvent.OnConfirmPasswordChanged ->
                _state.update { it.copy(confirmPassword = event.value, confirmPasswordError = null) }

            RegisterEvent.OnTogglePasswordVisibility ->
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

            RegisterEvent.OnToggleConfirmPasswordVisibility ->
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }

            RegisterEvent.OnRegisterClicked -> onRegisterClicked()
            RegisterEvent.OnGoToLoginClicked -> emitEffect(RegisterEffect.NavigateToLogin)
        }
    }

    private fun onRegisterClicked() {
        val current = _state.value

        val emailValidation = validateEmailUseCase(current.email)
        val passwordValidation = validatePasswordUseCase(current.password)
        val emailError = (emailValidation as? Result.Error)?.exception as? AuthException
        val passwordError = (passwordValidation as? Result.Error)?.exception as? AuthException
        val confirmError = if (current.password != current.confirmPassword) {
            Res.string.register_error_passwords_dont_match
        } else null

        if (emailError != null || passwordError != null || confirmError != null) {
            _state.update {
                it.copy(
                    emailError = emailError?.toMessageRes(),
                    passwordError = passwordError?.toMessageRes(),
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = registerWithEmailUseCase(current.email, current.password)
            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> _effect.emit(RegisterEffect.NavigateToHome)
                is Result.Error -> {
                    val authException = result.exception as? AuthException
                        ?: AuthException.Unknown(result.exception)
                    _effect.emit(RegisterEffect.ShowError(authException.toMessageRes()))
                }
            }
        }
    }

    private fun emitEffect(effect: RegisterEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}