package com.tiago.kmpauthflows.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.usecase.LogoutUseCase
import com.tiago.kmpauthflows.domain.usecase.ObserveAuthStateUseCase
import com.tiago.kmpauthflows.domain.util.Result
import com.tiago.kmpauthflows.presentation.common.toMessageRes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            observeAuthStateUseCase().collect { user ->
                _state.update { it.copy(user = user, isCheckingAuthState = false) }
                if (user == null) {
                    _effect.emit(HomeEffect.NavigateToLogin)
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnLogoutClicked -> onLogoutClicked()
        }
    }

    private fun onLogoutClicked() {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingOut = true) }
            val result = logoutUseCase()
            _state.update { it.copy(isLoggingOut = false) }

            if (result is Result.Error) {
                val authException = result.exception as? AuthException
                    ?: AuthException.Unknown(result.exception)
                _effect.emit(HomeEffect.ShowError(authException.toMessageRes()))
            }
            // si logout fue exitoso, NO navegamos manualmente acá —
            // observeAuthStateUseCase ya va a emitir null solo, y el
            // collect de arriba dispara NavigateToLogin automáticamente
        }
    }
}