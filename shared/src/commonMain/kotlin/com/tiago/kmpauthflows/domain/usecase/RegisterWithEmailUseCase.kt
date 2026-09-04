package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.repository.IAuthRepository
import com.tiago.kmpauthflows.domain.util.Result

class RegisterWithEmailUseCase(
    private val repository: IAuthRepository,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        val emailCheck = validateEmail(email)
        if (emailCheck is Result.Error) return emailCheck

        val passwordCheck = validatePassword(password)
        if (passwordCheck is Result.Error) return passwordCheck

        return repository.registerWithEmail(email.trim(), password)
    }
}