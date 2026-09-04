package com.tiago.kmpauthflows.presentation.home

import app.cash.turbine.test
import com.tiago.kmpauthflows.domain.fake.FakeAuthRepository
import com.tiago.kmpauthflows.domain.model.AuthProvider
import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.usecase.LogoutUseCase
import com.tiago.kmpauthflows.domain.usecase.ObserveAuthStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeRepository = FakeAuthRepository()

    private fun createViewModel() = HomeViewModel(
        observeAuthStateUseCase = ObserveAuthStateUseCase(fakeRepository),
        logoutUseCase = LogoutUseCase(fakeRepository)
    )

    @BeforeTest
    fun setup() { Dispatchers.setMain(testDispatcher) }

    @AfterTest
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `con sesion activa, expone el User y deja de chequear`() = runTest {
        val user = User(id = "1", email = "tiago@example.com", displayName = "Tiago", photoUrl = null, provider = AuthProvider.EMAIL)
        fakeRepository.emitAuthState(user)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(user, viewModel.state.value.user)
        assertFalse(viewModel.state.value.isCheckingAuthState)
    }

    @Test
    fun `sin sesion, emite NavigateToLogin`() = runTest {
        fakeRepository.emitAuthState(null)
        val viewModel = createViewModel()

        viewModel.effect.test {
            advanceUntilIdle()
            assertEquals(HomeEffect.NavigateToLogin, awaitItem())
        }
    }

    @Test
    fun `logout exitoso NO emite NavigateToLogin manualmente`() = runTest {
        val user = User(id = "1", email = "tiago@example.com", displayName = "Tiago", photoUrl = null, provider = AuthProvider.EMAIL)
        fakeRepository.emitAuthState(user)
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onEvent(HomeEvent.OnLogoutClicked)
            fakeRepository.emitAuthState(null) // simula lo que Firebase haría solo tras el signOut()

            assertEquals(HomeEffect.NavigateToLogin, awaitItem())
            // si HomeViewModel emitiera NavigateToLogin manualmente TAMBIÉN,
            // este test detectaría un segundo evento inesperado
        }

        assertNull(viewModel.state.value.user)
    }
}