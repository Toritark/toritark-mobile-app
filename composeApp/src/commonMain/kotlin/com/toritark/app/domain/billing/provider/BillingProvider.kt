package com.toritark.app.domain.billing.provider

import co.touchlab.kermit.Logger
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.toritark.app.domain.core.debug.IsDebug

internal interface BillingProvider {
    suspend fun initialize(userId: Long)
}

internal class BillingProviderImpl(
    private val isDebug: IsDebug,
) : BillingProvider {

    private val logger = Logger.withTag(LOG_TAG)

    override suspend fun initialize(userId: Long) {
        logger.d { "initialize: userId=$userId" }

        Purchases.logLevel = if (isDebug()) LogLevel.VERBOSE else LogLevel.WARN
        Purchases.configure(
            apiKey = getRevenueCatApiKey(),
        ) {
            appUserId = userId.toString()
        }
    }

    private companion object {
        private const val LOG_TAG = "BillingProvider"
    }
}

internal expect suspend fun getRevenueCatApiKey(): String