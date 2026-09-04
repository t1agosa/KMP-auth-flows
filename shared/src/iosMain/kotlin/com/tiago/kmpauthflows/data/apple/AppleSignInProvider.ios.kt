package com.tiago.kmpauthflows.data.apple

import com.tiago.kmpauthflows.data.model.FirebaseUserData

/**
 * TODO (requiere Mac): implementar con ASAuthorizationAppleIDProvider (nativo).
 *
 * A diferencia de Android, en iOS SÍ se obtienen idToken + nonce por separado
 * (vía ASAuthorizationAppleIDCredential), que después se le pasan a
 * FirebaseAuthService.signInWithAppleCredential(idToken, rawNonce) para
 * completar el login — ese método de FirebaseAuthService ya está listo,
 * pensado justamente para este caso.
 *
 * Pasos pendientes:
 * 1. Implementar ASAuthorizationControllerDelegate vía cinterop.
 * 2. Generar y hashear el nonce (SHA256) antes de pedir la credencial,
 *    requisito de seguridad de Apple.
 * 3. Extraer idToken del ASAuthorizationAppleIDCredential recibido.
 * 4. Llamar a FirebaseAuthService.signInWithAppleCredential(idToken, rawNonce).
 */
actual class AppleSignInProvider {
    actual suspend fun signIn(): FirebaseUserData =
        throw NotImplementedError("Pendiente: implementar con Mac. Ver TODO en este archivo.")
}