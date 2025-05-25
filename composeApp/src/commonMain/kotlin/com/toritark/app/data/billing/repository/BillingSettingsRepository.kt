package com.toritark.app.data.billing.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal interface BillingSettingsRepository {
    suspend fun getLastPaywallShowAt(): Long
    suspend fun setLastPaywallShowNow()
}

internal class BillingSettingsRepositoryImpl(
    private val settings: Settings,
    private val ioDispatcher: CoroutineDispatcher,
) : BillingSettingsRepository {

    override suspend fun getLastPaywallShowAt(): Long {
        return withContext(ioDispatcher) {
            settings.getLong(KEY_LAST_PAYWALL_SHOW_AT, 0)
        }
    }

    override suspend fun setLastPaywallShowNow() {
        withContext(ioDispatcher) {
            settings.putLong(KEY_LAST_PAYWALL_SHOW_AT, Clock.System.now().toEpochMilliseconds())
        }
    }

    private companion object {
        private const val KEY_LAST_PAYWALL_SHOW_AT = "last_paywall_show_at"
    }
}