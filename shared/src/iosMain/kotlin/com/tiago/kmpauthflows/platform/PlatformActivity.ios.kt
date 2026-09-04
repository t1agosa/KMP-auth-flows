package com.tiago.kmpauthflows.platform

import androidx.compose.runtime.Composable

actual class PlatformActivity

@Composable
actual fun rememberCurrentPlatformActivity(): PlatformActivity = PlatformActivity()