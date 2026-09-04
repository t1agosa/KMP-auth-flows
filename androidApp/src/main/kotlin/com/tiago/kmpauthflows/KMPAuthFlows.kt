package com.tiago.kmpauthflows

import android.app.Application
import com.tiago.kmpauthflows.di.initKoin

import com.tiago.kmpauthflows.data.android.ActivityProvider

class KmpAuthFlowsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val activityProvider = ActivityProvider()
        registerActivityLifecycleCallbacks(activityProvider)
        initKoin {
            // el módulo de Koin va a exponer este mismo activityProvider (Fase 4)
        }
    }
}