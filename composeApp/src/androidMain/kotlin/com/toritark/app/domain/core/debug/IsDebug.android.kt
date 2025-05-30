@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.core.debug

import com.toritark.app.BuildConfig

internal actual class IsDebugImpl actual constructor(): IsDebug {
    actual override fun invoke(): Boolean {
        return BuildConfig.DEBUG
    }
}