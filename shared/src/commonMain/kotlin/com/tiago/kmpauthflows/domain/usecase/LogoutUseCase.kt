package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.repository.AuthRepository
import com.tiago.kmpauthflows.domain.util.Result

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.logout()
}