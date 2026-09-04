package com.tiago.kmpauthflows.data.google

/**
 * TODO (requiere Mac): implementar con el SDK nativo de Google Sign-In para iOS,
 * o con el flujo de Firebase vía OAuthProvider si se prefiere evitar un pod extra.
 *
 * Pasos pendientes:
 * 1. Agregar el pod GoogleSignIn (o resolverlo 100% vía Firebase OAuthProvider,
 *    evaluar cuál conviene una vez en Mac).
 * 2. Configurar el URL scheme reverso en Info.plist (lo provee GoogleService-Info.plist).
 * 3. Implementar requestIdToken() usando GIDSignIn.sharedInstance con
 *    suspendCancellableCoroutine.
 */
actual class GoogleSignInProvider {
    actual suspend fun requestIdToken(): String =
        throw NotImplementedError("Pendiente: implementar con Mac. Ver TODO en este archivo.")
}