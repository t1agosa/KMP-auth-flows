package com.tiago.kmpauthflows.domain.fake

import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.repository.IAuthRepository
import com.tiago.kmpauthflows.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthRepository : IAuthRepository {

    var userToReturn: User? = null
    var exceptionToThrow: AuthException? = null

    var logoutCalled = false
        private set
    var lastEmailUsed: String? = null
        private set
    var googleLoginCalled = false
        private set
    var appleLoginCalled = false
        private set

    private val authState = MutableStateFlow<User?>(null)

    override suspend fun loginWithEmail(email: String, password: String): Result<User> {
        lastEmailUsed = email
        return resultForUser()
    }

    override suspend fun registerWithEmail(email: String, password: String): Result<User> {
        lastEmailUsed = email
        return resultForUser()
    }

    override suspend fun loginWithGoogle(): Result<User> {
        googleLoginCalled = true
        return resultForUser()
    }

    override suspend fun loginWithApple(): Result<User> {
        appleLoginCalled = true
        return resultForUser()
    }

    override suspend fun logout(): Result<Unit> {
        logoutCalled = true
        val error = exceptionToThrow
        return if (error != null) Result.Error(error) else Result.Success(Unit)
    }

    override fun observeAuthState(): Flow<User?> = authState

    fun emitAuthState(user: User?) {
        authState.value = user
    }

    private fun resultForUser(): Result<User> {
        val error = exceptionToThrow
        return if (error != null) {
            Result.Error(error)
        } else {
            Result.Success(
                userToReturn ?: error("FakeAuthRepository: falta configurar userToReturn en este test")
            )
        }
    }
}