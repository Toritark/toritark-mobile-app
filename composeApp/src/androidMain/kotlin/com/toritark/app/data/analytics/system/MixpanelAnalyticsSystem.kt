package com.toritark.app.data.analytics.system

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty
import org.json.JSONObject

internal class MixpanelAnalyticsSystem(
    private val context: Context,
) : AnalyticsSystem(name = "Mixpanel") {

    private var mixpanel: MixpanelAPI? = null

    override fun initialize() {
        mixpanel = MixpanelAPI.getInstance(
            context,
            BuildKonfig.MIXPANEL_API_KEY,
            true,
        )
    }

    override fun setUserId(userId: String) {
        mixpanel?.identify(userId)
    }

    override fun setPropertyInternal(property: AnalyticsProperty) {
        mixpanel?.people?.set(property.name, property.value)
    }

    override fun logEventInternal(event: AnalyticsEvent) {
        val eventProperties = JSONObject().apply {
            event.parameters.forEach { (key, value) ->
                put(key, value)
            }
        }

        mixpanel?.track(event.name, eventProperties)
    }

    override fun logScreenViewInternal(screenName: String) {
        logScreenViewAsEvent(screenName)
    }
}