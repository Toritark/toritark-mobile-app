@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.core.debug

interface IsDebug {
    operator fun invoke(): Boolean
}

internal expect class IsDebugImpl() : IsDebug {
    override fun invoke(): Boolean
}