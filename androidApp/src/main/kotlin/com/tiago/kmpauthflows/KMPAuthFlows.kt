package com.tiago.kmpauthflows

import android.app.Application
import com.tiago.kmpauthflows.di.initKoin

class KmpAuthFlowsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}