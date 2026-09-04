package com.tiago.kmpauthflows.domain.usecase

import com.tiago.kmpauthflows.domain.model.User
import com.tiago.kmpauthflows.domain.repository.AuthRepository
import com.tiago.kmpauthflows.domain.util.Result
import com.tiago.kmpauthflows.platform.PlatformActivity

class LoginWithGoogleUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(activity: PlatformActivity): Result<User> =
        repository.loginWithGoogle(activity)
}