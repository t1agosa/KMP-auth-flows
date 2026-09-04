// commonMain
package com.tiago.kmpauthflows.data.apple

import com.tiago.kmpauthflows.data.model.FirebaseUserData
import com.tiago.kmpauthflows.platform.PlatformActivity

expect class AppleSignInProvider {
    suspend fun signIn(activity: PlatformActivity): FirebaseUserData
}