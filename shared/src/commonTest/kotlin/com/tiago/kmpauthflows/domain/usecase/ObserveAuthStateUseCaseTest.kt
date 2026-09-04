package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.fake.FakeAuthRepository
import com.tiago.kmpauthflows.domain.model.AuthProvider
import com.tiago.kmpauthflows.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ObserveAuthStateUseCaseTest {

    private val fakeRepository = FakeAuthRepository()
    private val observeAuthState = ObserveAuthStateUseCase(fakeRepository)

    @Test
    fun `sin sesion activa emite null`() = runTest {
        val current = observeAuthState().first()

        assertNull(current)
    }

    @Test
    fun `con sesion activa emite el User actual`() = runTest {
        val user = User(
            id = "1",
            email = "tiago@example.com",
            displayName = "Tiago",
            photoUrl = null,
            provider = AuthProvider.EMAIL
        )
        fakeRepository.emitAuthState(user)

        val current = observeAuthState().first()

        assertEquals(user, current)
    }
}