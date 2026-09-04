package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.util.Result
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ValidatePasswordUseCaseTest {

    private val validatePassword = ValidatePasswordUseCase()

    @Test
    fun `password de 8 caracteres o mas devuelve Success`() {
        val result = validatePassword("password123")
        assertTrue(result is Result.Success)
    }

    @Test
    fun `password de menos de 8 caracteres devuelve Error WeakPassword`() {
        val result = validatePassword("1234567")
        assertIs<Result.Error>(result)
        assertIs<AuthException.WeakPassword>(result.exception)
    }
}