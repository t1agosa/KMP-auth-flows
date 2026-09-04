package com.tiago.kmpauthflows.platform

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable

actual typealias PlatformActivity = android.app.Activity


@Composable
actual fun rememberCurrentPlatformActivity(): PlatformActivity {
    return LocalActivity.current
        ?: error("rememberCurrentPlatformActivity() se llamó fuera de una Activity")
}