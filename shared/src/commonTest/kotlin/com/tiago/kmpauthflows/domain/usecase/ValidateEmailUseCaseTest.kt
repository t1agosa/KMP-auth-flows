package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.util.Result
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ValidateEmailUseCaseTest {

    private val validateEmail = ValidateEmailUseCase()

    @Test
    fun `email valido devuelve Success`() {
        val result = validateEmail("tiago@example.com")
        assertTrue(result is Result.Success)
    }

    @Test
    fun `email sin arroba devuelve Error InvalidEmailFormat`() {
        val result = validateEmail("tiagoexample.com")
        assertIs<Result.Error>(result)
        assertIs<AuthException.InvalidEmailFormat>(result.exception)
    }

    @Test
    fun `email vacio devuelve Error InvalidEmailFormat`() {
        val result = validateEmail("   ")
        assertIs<Result.Error>(result)
        assertIs<AuthException.InvalidEmailFormat>(result.exception)
    }
}