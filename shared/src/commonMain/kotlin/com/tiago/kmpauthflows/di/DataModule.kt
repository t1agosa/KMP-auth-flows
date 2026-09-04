package com.tiago.kmpauthflows.di

import com.tiago.kmpauthflows.data.firebase.FirebaseAuthService
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuthService() }
}