package com.toritark.app.data.core.model

import com.toritark.app.BuildKonfig

sealed class Environment {
    data object Local : Environment()
    data object Production : Environment()

    companion object {
        private const val ENVIRONMENT_LOCAL = "local"
        private const val ENVIRONMENT_PRODUCTION = "production"

        val current = when (BuildKonfig.ENVIRONMENT) {
            ENVIRONMENT_LOCAL -> Local
            ENVIRONMENT_PRODUCTION -> Production
            else -> throw IllegalStateException("Unknown environment: \"${BuildKonfig.ENVIRONMENT}\"")
        }
    }
}