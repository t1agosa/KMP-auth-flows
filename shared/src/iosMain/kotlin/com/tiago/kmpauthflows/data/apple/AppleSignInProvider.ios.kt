// GoogleSignInProvider.ios.kt
package com.tiago.kmpauthflows.data.google

import com.tiago.kmpauthflows.platform.PlatformActivity

actual class GoogleSignInProvider {
    actual suspend fun requestIdToken(activity: PlatformActivity): String =
        throw NotImplementedError("Pendiente: implementar con Mac. Ver TODO en este archivo.")
}