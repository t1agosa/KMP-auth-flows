package com.tiago.kmpauthflows.data.repository

import com.tiago.kmpauthflows.data.apple.AppleSignInProvider
import com.tiago.kmpauthflows.data.firebase.FirebaseAuthService
import com.tiago.kmpauthflows.data.google.GoogleSignInProvider
import com.tiago.kmpauthflows.data.model.FirebaseUserData
import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.domain.model.AuthProvider
import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.repository.IAuthRepository
import com.tiago.kmpauthflows.domain.util.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService,
    private val googleSignInProvider: GoogleSignInProvider,
    private val appleSignInProvider: AppleSignInProvider
) : IAuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): Result<User> =
        runCatchingAuth {
            firebaseAuthService.signInWithEmail(email, password).toUser(AuthProvider.EMAIL)
        }

    override suspend fun registerWithEmail(email: String, password: String): Result<User> =
        runCatchingAuth {
            firebaseAuthService.signUpWithEmail(email, password).toUser(AuthProvider.EMAIL)
        }

    override suspend fun loginWithGoogle(): Result<User> = runCatchingAuth {
        val idToken = googleSignInProvider.requestIdToken()
        firebaseAuthService.signInWithGoogleIdToken(idToken).toUser(AuthProvider.GOOGLE)
    }

    override suspend fun loginWithApple(): Result<User> = runCatchingAuth {
        appleSignInProvider.signIn().toUser(AuthProvider.APPLE)
    }

    override suspend fun logout(): Result<Unit> = runCatchingAuth {
        firebaseAuthService.signOut()
    }

    override fun observeAuthState(): Flow<User?> =
        firebaseAuthService.observeAuthState().map { data -> data?.toUser(data.providerId.toAuthProvider()) }

    private inline fun <T> runCatchingAuth(block: () -> T): Result<T> = try {
        Result.Success(block())
    } catch (e: CancellationException) {
        throw e // nunca capturar la cancelación — ver result_pattern.md
    } catch (e: AuthException) {
        Result.Error(e) // ya viene mapeada desde FirebaseAuthService/los providers
    } catch (e: Exception) {
        Result.Error(AuthException.Unknown(e))
    }
}

private fun FirebaseUserData.toUser(provider: AuthProvider): User = User(
    id = id,
    email = email,
    displayName = displayName,
    photoUrl = photoUrl,
    provider = provider
)

private fun String?.toAuthProvider(): AuthProvider = when (this) {
    "google.com" -> AuthProvider.GOOGLE
    "apple.com" -> AuthProvider.APPLE
    else -> AuthProvider.EMAIL // "password", null, o cualquier otro caso no contemplado
}