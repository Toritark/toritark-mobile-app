@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.core.debug

import com.toritark.app.BuildConfig

internal actual class IsDebugImpl : IsDebug {
    actual override fun invoke(): Boolean {
        return BuildConfig.DEBUG
    }
}