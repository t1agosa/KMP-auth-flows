package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(private val repository: IAuthRepository) {
    operator fun invoke(): Flow<User?> = repository.observeAuthState()
}