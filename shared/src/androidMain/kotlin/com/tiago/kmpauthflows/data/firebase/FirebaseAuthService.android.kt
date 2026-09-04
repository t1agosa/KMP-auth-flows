package com.tiago.kmpauthflows.data.firebase

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.data.model.FirebaseUserData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

actual class FirebaseAuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    actual suspend fun signInWithEmail(email: String, password: String): FirebaseUserData =
        runCatchingFirebase {
            auth.signInWithEmailAndPassword(email, password).await().user.toFirebaseUserData()
        }

    actual suspend fun signUpWithEmail(email: String, password: String): FirebaseUserData =
        runCatchingFirebase {
            auth.createUserWithEmailAndPassword(email, password).await().user.toFirebaseUserData()
        }

    actual suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUserData =
        runCatchingFirebase {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await().user.toFirebaseUserData()
        }

    actual suspend fun signInWithAppleCredential(idToken: String, rawNonce: String): FirebaseUserData =
        runCatchingFirebase {
            val credential = OAuthProvider.newCredentialBuilder("apple.com")
                .setIdTokenWithRawNonce(idToken, rawNonce)
                .build()
            auth.signInWithCredential(credential).await().user.toFirebaseUserData()
        }

    actual suspend fun signOut() {
        auth.signOut()
    }

    actual fun observeAuthState(): Flow<FirebaseUserData?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.toFirebaseUserData())
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    private inline fun <T> runCatchingFirebase(block: () -> T): T = try {
        block()
    } catch (e: CancellationException) {
        throw e // nunca capturar la cancelación — ver result_pattern.md
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        throw AuthException.InvalidCredentials
    } catch (e: FirebaseAuthInvalidUserException) {
        throw AuthException.UserNotFound
    } catch (e: FirebaseAuthUserCollisionException) {
        throw AuthException.EmailAlreadyInUse
    } catch (e: FirebaseAuthWeakPasswordException) {
        throw AuthException.WeakPassword
    } catch (e: FirebaseNetworkException) {
        throw AuthException.NetworkError
    } catch (e: Exception) {
        throw AuthException.Unknown(e)
    }
}

private fun FirebaseUser?.toFirebaseUserData(): FirebaseUserData {
    val user = this ?: throw AuthException.Unknown(IllegalStateException("Firebase no devolvió un usuario"))
    return FirebaseUserData(
        id = user.uid,
        email = user.email,
        displayName = user.displayName,
        photoUrl = user.photoUrl?.toString()
    )
}