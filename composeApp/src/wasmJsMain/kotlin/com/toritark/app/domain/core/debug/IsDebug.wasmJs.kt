package com.toritark.app.domain.core.debug

internal actual class IsDebugImpl actual constructor() : IsDebug {

    actual override operator fun invoke(): Boolean {
        return true // FIXME
    }
}