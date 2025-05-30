package com.toritark.app.data.analytics.system

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.android.events.Identify
import com.amplitude.common.Logger
import com.amplitude.core.ServerZone
import com.toritark.app.BuildKonfig
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty
import com.toritark.app.domain.core.debug.IsDebug

internal class AmplitudeAnalyticsSystem(
    private val context: Context,
    private val isDebug: IsDebug,
) : AnalyticsSystem(
    name = "Amplitude"
) {

    private var amplitude: Amplitude? = null

    override fun initialize() {
        logger.d { "initialize" }

        amplitude = Amplitude(
            Configuration(
                apiKey = BuildKonfig.AMPLITUDE_API_KEY,
                context = context,
                serverZone = ServerZone.EU,
            )
        ).apply {
            logger.logMode = if (isDebug()) Logger.LogMode.DEBUG else Logger.LogMode.OFF
        }

        logger.d { "initialize: done" }
    }

    override fun setUserId(userId: String) {
        amplitude?.setUserId(userId)
    }

    override fun setPropertyInternal(property: AnalyticsProperty) {
        val identify = Identify().apply {
            if (property.value != null) {
                set(property.name, property.value)
            } else {
                unset(property.name)
            }
        }

        amplitude?.identify(identify)
    }

    override fun logEventInternal(event: AnalyticsEvent) {
        amplitude?.track(
            event.name,
            event.parameters,
        )
    }

    override fun logScreenViewInternal(screenName: String) {
        logScreenViewAsEvent(screenName)
    }

    override fun logAdRevenue(
        format: String,
        source: String,
        adUnitName: String,
        amount: Double,
        currency: String,
    ) {
        // TODO
    }
}