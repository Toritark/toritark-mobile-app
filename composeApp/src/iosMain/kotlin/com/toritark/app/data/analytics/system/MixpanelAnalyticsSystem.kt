package com.toritark.app.data.analytics.system

import cocoapods.Mixpanel.Mixpanel
import com.toritark.app.BuildKonfig
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty
import com.toritark.app.domain.core.debug.IsDebug

internal class MixpanelAnalyticsSystem(
    private val isDebug: IsDebug,
) : AnalyticsSystem(name = "Mixpanel") {

    override fun initialize() {
        Mixpanel.sharedInstanceWithToken(
            apiToken = BuildKonfig.MIXPANEL_API_KEY,
            trackAutomaticEvents = true,
        ).apply {
            enableLogging = isDebug()
        }
    }

    override fun setUserId(userId: String) {
        Mixpanel.sharedInstance()?.identify(
            distinctId = userId,
        )
    }

    override fun setPropertyInternal(property: AnalyticsProperty) {
        if (property.value == null) {
            return
        }

        Mixpanel.sharedInstance()?.people()?.set(
            property = property.name,
            to = property.value,
        )
    }

    override fun logEventInternal(event: AnalyticsEvent) {
        Mixpanel.sharedInstance()?.track(
            event = event.name,
            properties = event.parameters.mapKeys { it.key as Any? },
        )
    }

    override fun logAdRevenue(
        format: String,
        source: String,
        adUnitName: String,
        amount: Double,
        currency: String,
    ) {

    }
}