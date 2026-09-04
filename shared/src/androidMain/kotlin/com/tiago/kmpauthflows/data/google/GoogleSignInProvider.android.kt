package com.tiago.kmpauthflows.data.google

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.tiago.kmpauthflows.domain.model.AuthException
import com.tiago.kmpauthflows.platform.PlatformActivity
import kotlinx.coroutines.CancellationException

actual class GoogleSignInProvider(private val webClientId: String) {

    actual suspend fun requestIdToken(activity: PlatformActivity): String {
        val credentialManager = CredentialManager.create(activity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val response = credentialManager.getCredential(activity, request)
            val credential = GoogleIdTokenCredential.createFrom(response.credential.data)
            credential.idToken
        } catch (e: CancellationException) {
            throw e
        } catch (e: GetCredentialCancellationException) {
            throw AuthException.SignInCancelled
        } catch (e: GetCredentialException) {
            throw AuthException.Unknown(e)
        }
    }
}