package com.toritark.stories.domain.core.debug

interface IsDebug {
    fun execute(): Boolean
}

internal class IsDebugImpl : IsDebug {

    override fun execute(): Boolean {
        return true
    }
}