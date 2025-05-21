package com.toritark.app.domain.core.env

import com.toritark.app.BuildKonfig
import com.toritark.app.data.core.model.Environment
import com.toritark.app.data.core.model.Environment.Local
import com.toritark.app.data.core.model.Environment.Production

interface GetEnvironment {
    operator fun invoke(): Environment
}

internal class GetEnvironmentImpl : GetEnvironment {

    val currentEnvironment by lazy(mode = LazyThreadSafetyMode.NONE) {
        when (BuildKonfig.ENVIRONMENT) {
            ENVIRONMENT_LOCAL -> Local
            ENVIRONMENT_PRODUCTION -> Production
            else -> throw IllegalStateException("Unknown environment: \"${BuildKonfig.ENVIRONMENT}\"")
        }
    }

    override operator fun invoke(): Environment {
        return currentEnvironment
    }

    private companion object {
        private const val ENVIRONMENT_LOCAL = "local"
        private const val ENVIRONMENT_PRODUCTION = "production"
    }
}