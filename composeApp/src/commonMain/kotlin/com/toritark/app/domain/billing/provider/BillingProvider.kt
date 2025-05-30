package com.toritark.app.domain.billing.provider

import co.touchlab.kermit.Logger
import com.toritark.app.data.core.model.Environment
import com.toritark.app.domain.core.debug.IsDebug
import com.toritark.app.domain.core.env.GetEnvironment

internal interface BillingProvider {
    suspend fun initialize(userId: Long)
}

abstract class BaseBillingProviderImpl : BillingProvider {
    protected val logger = Logger.withTag(LOG_TAG)

    protected abstract val isDebug: IsDebug
    protected abstract val getEnvironment: GetEnvironment

    protected fun getPrefixedUserId(userId: Long): String {
        val prefix = when (getEnvironment()) {
            Environment.Local -> USER_ID_PREFIX_LOCAL
            Environment.Production -> USER_ID_PREFIX_PRODUCTION
        }

        return "$prefix$userId"
    }

    protected companion object {
        private const val LOG_TAG = "BillingProvider"

        private const val USER_ID_PREFIX_LOCAL = "local_"
        private const val USER_ID_PREFIX_PRODUCTION = "production_"
    }
}

internal expect class BillingProviderImpl : BaseBillingProviderImpl {
    override val isDebug: IsDebug
    override val getEnvironment: GetEnvironment

    override suspend fun initialize(userId: Long)
}

//internal class BillingProviderImpl(
//    private val isDebug: IsDebug,
//    private val getEnvironment: GetEnvironment,
//) : BillingProvider {
//
//    private val logger = Logger.withTag(LOG_TAG)
//
//    override suspend fun initialize(userId: Long) {
//        logger.i { "initialize: userId=$userId" }
//
//        val prefixedUserId = getPrefixedUserId(userId)
//
//        logger.i { "initialize: prefixedUserId=$prefixedUserId" }
//
//        Purchases.logLevel = if (isDebug()) LogLevel.VERBOSE else LogLevel.WARN
//        Purchases.configure(
//            apiKey = BuildKonfig.REVENUE_CAT_API_KEY,
//        ) {
//            appUserId = prefixedUserId
//        }
//    }
//
//    private fun getPrefixedUserId(userId: Long): String {
//        val prefix = when (getEnvironment()) {
//            Environment.Local -> USER_ID_PREFIX_LOCAL
//            Environment.Production -> USER_ID_PREFIX_PRODUCTION
//        }
//
//        return "$prefix$userId"
//    }
//
//    private companion object {
//        private const val LOG_TAG = "BillingProvider"
//
//        private const val USER_ID_PREFIX_LOCAL = "local_"
//        private const val USER_ID_PREFIX_PRODUCTION = "production_"
//    }
//}