package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.repository.IAuthRepository
import com.tiago.kmpauthflows.domain.util.Result

class LogoutUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.logout()
}