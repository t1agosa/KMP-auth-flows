package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.util.Result

class ValidatePasswordUseCase {
    operator fun invoke(password: String): Result<Unit> {
        return if (password.length < MIN_LENGTH) {
            Result.Error(AuthException.WeakPassword)
        } else {
            Result.Success(Unit)
        }
    }

    private companion object {
        const val MIN_LENGTH = 8
    }
}