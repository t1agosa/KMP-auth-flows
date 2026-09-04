package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.repository.IAuthRepository
import com.tiago.kmpauthflows.domain.util.Result

class LoginWithGoogleUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(): Result<User> = repository.loginWithGoogle()
}