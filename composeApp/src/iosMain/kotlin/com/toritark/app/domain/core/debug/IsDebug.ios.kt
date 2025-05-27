@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalNativeApi::class)

package com.toritark.app.domain.core.debug

import kotlin.experimental.ExperimentalNativeApi

internal actual class IsDebugImpl : IsDebug {

    actual override fun invoke(): Boolean {
        return Platform.isDebugBinary
    }
}