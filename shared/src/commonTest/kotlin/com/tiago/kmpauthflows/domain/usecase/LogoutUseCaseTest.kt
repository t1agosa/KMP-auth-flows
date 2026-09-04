package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.fake.FakeAuthRepository
import com.tiago.kmpauthflows.domain.util.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class LogoutUseCaseTest {

    private val fakeRepository = FakeAuthRepository()
    private val logout = LogoutUseCase(fakeRepository)

    @Test
    fun `invoca logout en el repositorio y devuelve Success`() = runTest {
        val result = logout()

        assertIs<Result.Success<Unit>>(result)
        assertTrue(fakeRepository.logoutCalled)
    }
}