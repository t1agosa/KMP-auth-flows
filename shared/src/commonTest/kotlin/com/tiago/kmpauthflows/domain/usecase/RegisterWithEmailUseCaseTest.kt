package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.fake.FakeAuthRepository
import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.model.AuthProvider
import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.util.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

class RegisterWithEmailUseCaseTest {

    private val fakeRepository = FakeAuthRepository()
    private val registerWithEmail = RegisterWithEmailUseCase(
        repository = fakeRepository,
        validateEmail = ValidateEmailUseCase(),
        validatePassword = ValidatePasswordUseCase()
    )

    @Test
    fun `con datos validos delega al repositorio y devuelve Success`() = runTest {
        fakeRepository.userToReturn = User(
            id = "1",
            email = "nuevo@example.com",
            displayName = null,
            photoUrl = null,
            provider = AuthProvider.EMAIL
        )

        val result = registerWithEmail("nuevo@example.com", "password123")

        assertIs<Result.Success<User>>(result)
    }

    @Test
    fun `si el email ya esta en uso propaga el error del repositorio`() = runTest {
        fakeRepository.exceptionToThrow = AuthException.EmailAlreadyInUse

        val result = registerWithEmail("existente@example.com", "password123")

        assertIs<Result.Error>(result)
        assertIs<AuthException.EmailAlreadyInUse>(result.exception)
    }
}