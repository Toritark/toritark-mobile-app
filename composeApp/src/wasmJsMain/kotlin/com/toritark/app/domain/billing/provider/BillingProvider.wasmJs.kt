package com.toritark.app.domain.billing.provider

import com.toritark.app.domain.core.debug.IsDebug
import com.toritark.app.domain.core.env.GetEnvironment

internal actual class BillingProviderImpl(
    actual override val isDebug: IsDebug,
    actual override val getEnvironment: GetEnvironment,
) : BaseBillingProviderImpl() {

    actual override suspend fun initialize(userId: Long) {
        logger.e { "initialize: not implemented" }

        // TODO
    }
}