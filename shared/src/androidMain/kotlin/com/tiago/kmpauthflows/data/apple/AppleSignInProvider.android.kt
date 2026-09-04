package com.tiago.kmpauthflows.data.apple

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.tiago.kmpauthflows.data.android.ActivityProvider
import com.tiago.kmpauthflows.data.firebase.runCatchingFirebase
import com.tiago.kmpauthflows.data.firebase.toFirebaseUserData
import com.tiago.kmpauthflows.data.model.FirebaseUserData
import kotlinx.coroutines.tasks.await

actual class AppleSignInProvider(private val activityProvider: ActivityProvider) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    actual suspend fun signIn(): FirebaseUserData = runCatchingFirebase {
        val activity = activityProvider.requireCurrentActivity()
        val provider = OAuthProvider.newBuilder("apple.com").apply {
            scopes = listOf("email", "name")
        }.build()

        val result = auth.startActivityForSignInWithProvider(activity, provider).await()
        result.user.toFirebaseUserData()
    }
}