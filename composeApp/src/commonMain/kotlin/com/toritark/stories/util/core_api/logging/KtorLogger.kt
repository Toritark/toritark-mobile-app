package com.toritark.stories.util.core_api.logging

import io.ktor.client.plugins.logging.*

internal class KtorLogger : Logger {

    private val logger = co.touchlab.kermit.Logger.withTag(LOG_TAG)

    override fun log(message: String) {
        logger.v { message }
    }

    private companion object {
        private const val LOG_TAG = "Ktor"
    }
}