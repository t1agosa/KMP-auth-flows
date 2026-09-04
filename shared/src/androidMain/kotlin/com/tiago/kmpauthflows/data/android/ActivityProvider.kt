package com.tiago.kmpauthflows.data.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * Mantiene una referencia débil a la Activity actualmente visible.
 * Se actualiza sola con los callbacks de ciclo de vida de la Application —
 * ver el registro en KmpAuthFlowsApp.onCreate().
 */
class ActivityProvider : Application.ActivityLifecycleCallbacks {

    private var currentActivityRef: WeakReference<Activity>? = null

    fun requireCurrentActivity(): Activity =
        currentActivityRef?.get()
            ?: error("No hay ninguna Activity visible en este momento")

    override fun onActivityResumed(activity: Activity) {
        currentActivityRef = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        if (currentActivityRef?.get() == activity) currentActivityRef = null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}