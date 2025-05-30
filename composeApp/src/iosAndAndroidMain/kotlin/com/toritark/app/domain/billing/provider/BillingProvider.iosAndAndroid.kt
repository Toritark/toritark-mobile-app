package com.toritark.app.domain.billing.provider

import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.toritark.app.BuildKonfig
import com.toritark.app.domain.core.debug.IsDebug
import com.toritark.app.domain.core.env.GetEnvironment

actual class BillingProviderImpl(
    actual override val isDebug: IsDebug,
    actual override val getEnvironment: GetEnvironment,
) : BaseBillingProviderImpl() {


    actual override suspend fun initialize(userId: Long) {
        logger.i { "initialize: userId=$userId" }

        val prefixedUserId = getPrefixedUserId(userId)

        logger.i { "initialize: prefixedUserId=$prefixedUserId" }

        Purchases.logLevel = if (isDebug()) LogLevel.VERBOSE else LogLevel.WARN
        Purchases.configure(
            apiKey = BuildKonfig.REVENUE_CAT_API_KEY,
        ) {
            appUserId = prefixedUserId
        }
    }

    private companion object {

        private const val USER_ID_PREFIX_LOCAL = "local_"
        private const val USER_ID_PREFIX_PRODUCTION = "production_"
    }
}