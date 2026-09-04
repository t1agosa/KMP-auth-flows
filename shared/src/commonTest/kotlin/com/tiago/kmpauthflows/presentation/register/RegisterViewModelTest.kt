package com.tiago.kmpauthflows.presentation.register

import app.cash.turbine.test
import com.tiago.kmpauthflows.domain.fake.FakeAuthRepository
import com.tiago.kmpauthflows.domain.model.AuthProvider
import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.usecase.RegisterWithEmailUseCase
import com.tiago.kmpauthflows.domain.usecase.ValidateEmailUseCase
import com.tiago.kmpauthflows.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeRepository = FakeAuthRepository()

    private fun createViewModel() = RegisterViewModel(
        validateEmailUseCase = ValidateEmailUseCase(),
        validatePasswordUseCase = ValidatePasswordUseCase(),
        registerWithEmailUseCase = RegisterWithEmailUseCase(fakeRepository, ValidateEmailUseCase(), ValidatePasswordUseCase())
    )

    @BeforeTest
    fun setup() { Dispatchers.setMain(testDispatcher) }

    @AfterTest
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `contraseñas distintas muestran confirmPasswordError y no llaman al repositorio`() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(RegisterEvent.OnEmailChanged("tiago@example.com"))
        viewModel.onEvent(RegisterEvent.OnPasswordChanged("password123"))
        viewModel.onEvent(RegisterEvent.OnConfirmPasswordChanged("otraPassword123"))
        viewModel.onEvent(RegisterEvent.OnRegisterClicked)

        assertNotNull(viewModel.state.value.confirmPasswordError)
        assertNull(fakeRepository.lastEmailUsed)
    }

    @Test
    fun `registro exitoso emite NavigateToHome`() = runTest {
        fakeRepository.userToReturn = User(
            id = "1", email = "nuevo@example.com", displayName = null,
            photoUrl = null, provider = AuthProvider.EMAIL
        )
        val viewModel = createViewModel()

        viewModel.effect.test {
            viewModel.onEvent(RegisterEvent.OnEmailChanged("nuevo@example.com"))
            viewModel.onEvent(RegisterEvent.OnPasswordChanged("password123"))
            viewModel.onEvent(RegisterEvent.OnConfirmPasswordChanged("password123"))
            viewModel.onEvent(RegisterEvent.OnRegisterClicked)

            assertEquals(RegisterEffect.NavigateToHome, awaitItem())
        }
    }
}