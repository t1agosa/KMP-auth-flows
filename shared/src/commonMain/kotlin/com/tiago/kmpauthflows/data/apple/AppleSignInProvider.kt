package com.tiago.kmpauthflows.data.apple

import com.tiago.kmpauthflows.data.model.FirebaseUserData

expect class AppleSignInProvider {
    /** Encapsula TODO el flujo — a diferencia de Google, acá no hay un idToken suelto que separar. */
    suspend fun signIn(): FirebaseUserData
}