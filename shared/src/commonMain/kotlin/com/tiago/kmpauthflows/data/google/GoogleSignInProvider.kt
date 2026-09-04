package com.tiago.kmpauthflows.data.google

expect class GoogleSignInProvider {
    /** Devuelve el idToken de Google, listo para pasarle a FirebaseAuthService.signInWithGoogleIdToken(). */
    suspend fun requestIdToken(): String
}