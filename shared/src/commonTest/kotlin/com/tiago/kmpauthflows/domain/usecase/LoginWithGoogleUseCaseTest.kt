package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.fake.FakeAuthRepository
import com.tiago.kmpauthflows.domain.model.AuthProvider
import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.util.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoginWithGoogleUseCaseTest {

    private val fakeRepository = FakeAuthRepository()
    private val loginWithGoogle = LoginWithGoogleUseCase(fakeRepository)

    @Test
    fun `delega el idToken al repositorio y devuelve el resultado tal cual`() = runTest {
        fakeRepository.userToReturn = User(
            id = "1",
            email = "tiago@gmail.com",
            displayName = "Tiago",
            photoUrl = null,
            provider = AuthProvider.GOOGLE
        )

        val result = loginWithGoogle("fake-google-id-token")

        assertIs<Result.Success<User>>(result)
        assertEquals("fake-google-id-token", fakeRepository.lastGoogleIdToken)
    }
}