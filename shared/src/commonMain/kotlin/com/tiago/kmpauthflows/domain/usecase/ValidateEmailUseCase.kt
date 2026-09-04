package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.util.Result

class ValidateEmailUseCase {
    operator fun invoke(email: String): Result<Unit> {
        val trimmed = email.trim()
        return if (trimmed.isBlank() || !EMAIL_REGEX.matches(trimmed)) {
            Result.Error(AuthException.InvalidEmailFormat)
        } else {
            Result.Success(Unit)
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}