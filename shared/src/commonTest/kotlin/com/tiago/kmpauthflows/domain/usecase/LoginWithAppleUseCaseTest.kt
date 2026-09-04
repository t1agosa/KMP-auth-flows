package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.fake.FakeAuthRepository
import com.tiago.kmpauthflows.domain.model.AuthProvider
import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.util.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoginWithAppleUseCaseTest {

    private val fakeRepository = FakeAuthRepository()
    private val loginWithApple = LoginWithAppleUseCase(fakeRepository)

    @Test
    fun `delega idToken y nonce al repositorio y devuelve el resultado tal cual`() = runTest {
        fakeRepository.userToReturn = User(
            id = "1",
            email = "tiago@icloud.com",
            displayName = "Tiago",
            photoUrl = null,
            provider = AuthProvider.APPLE
        )

        val result = loginWithApple("fake-apple-id-token", "fake-nonce")

        assertIs<Result.Success<User>>(result)
        assertEquals("fake-apple-id-token", fakeRepository.lastAppleIdToken)
    }
}