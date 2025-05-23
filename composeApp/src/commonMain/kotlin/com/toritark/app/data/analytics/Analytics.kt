package com.toritark.app.data.analytics

import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty
import com.toritark.app.data.analytics.system.AnalyticsSystem

object Analytics {
    private var analyticsSystems: List<AnalyticsSystem> = emptyList()

    fun initialize(
        analyticsSystems: List<AnalyticsSystem>,
    ) {
        this.analyticsSystems = analyticsSystems

        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.initialize() }
    }

    fun logEvent(eventName: String, parameters: Map<String, Any> = emptyMap()) {
        logEvent(AnalyticsEvent(eventName, parameters))
    }

    fun setProperty(property: AnalyticsProperty) {
        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.setProperty(property) }
    }

    fun logEvent(event: AnalyticsEvent) {
        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.logEvent(event) }
    }

    fun logScreenView(screenName: String) {
        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.logScreenView(screenName) }
    }
}