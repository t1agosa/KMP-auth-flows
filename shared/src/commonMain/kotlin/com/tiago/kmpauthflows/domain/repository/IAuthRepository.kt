package com.tiago.kmpauthflows.domain.repository

import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun loginWithEmail(email: String, password: String): Result<User>
    suspend fun registerWithEmail(email: String, password: String): Result<User>
    suspend fun loginWithGoogle(): Result<User>
    suspend fun loginWithApple(): Result<User>
    suspend fun logout(): Result<Unit>
    fun observeAuthState(): Flow<User?>
}