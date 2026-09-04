package com.tiago.kmpauthflows.domain.repository

import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun loginWithEmail(email: String, password: String): Result<User>
    suspend fun registerWithEmail(email: String, password: String): Result<User>
    suspend fun loginWithGoogle(idToken: String): Result<User>
    suspend fun loginWithApple(idToken: String, nonce: String): Result<User>
    suspend fun logout(): Result<Unit>

    // Reactivo — se observa mientras la sesión esté activa, no tiene un "desenlace" al que Result aplique.
    fun observeAuthState(): Flow<User?>
}