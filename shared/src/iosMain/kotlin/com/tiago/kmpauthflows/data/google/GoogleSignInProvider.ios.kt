// AppleSignInProvider.ios.kt
package com.tiago.kmpauthflows.data.apple

import com.tiago.kmpauthflows.data.model.FirebaseUserData
import com.tiago.kmpauthflows.platform.PlatformActivity

actual class AppleSignInProvider {
    actual suspend fun signIn(activity: PlatformActivity): FirebaseUserData =
        throw NotImplementedError("Pendiente: implementar con Mac. Ver TODO en este archivo.")
}