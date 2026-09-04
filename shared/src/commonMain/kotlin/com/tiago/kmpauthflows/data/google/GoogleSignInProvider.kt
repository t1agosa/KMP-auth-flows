package com.tiago.kmpauthflows.data.google

import com.tiago.kmpauthflows.platform.PlatformActivity

expect class GoogleSignInProvider {
    suspend fun requestIdToken(activity: PlatformActivity): String
}