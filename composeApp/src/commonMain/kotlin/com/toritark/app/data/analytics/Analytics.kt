package com.toritark.app.data.analytics

import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty
import com.toritark.app.data.analytics.system.AnalyticsSystem

object Analytics {
    private val logger = Logger.withTag("Analytics")


    private var analyticsSystems: List<AnalyticsSystem> = emptyList()

    fun initialize(
        analyticsSystems: List<AnalyticsSystem>,
    ) {
        logger.d { "initialize: ${analyticsSystems.size} systems" }

        this.analyticsSystems = analyticsSystems

        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.initialize() }
    }

    fun setUserId(userId: String) {
        logger.d { "setUserId: $userId" }

        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.setUserId(userId) }
    }

    fun logEvent(eventName: String, parameters: Map<String, Any?> = emptyMap()) {
        logEvent(AnalyticsEvent(eventName, parameters))
    }

    fun setProperty(property: AnalyticsProperty) {
        logger.d { "setProperty: property=$property" }

        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.setProperty(property) }
    }

    fun logEvent(event: AnalyticsEvent) {
        logger.d { "logEvent: event=$event" }

        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.logEvent(event) }
    }

    fun logScreenView(screenName: String) {
        logger.d { "logScreenView: screenName=$screenName" }

        analyticsSystems.forEach { analyticsSystem -> analyticsSystem.logScreenView(screenName) }
    }

    fun logAdRevenue(
        format: String,
        source: String,
        adUnitName: String,
        amount: Double,
        currency: String,
    ) {
        logger.d { "logAdRevenue: format=$format, source=$source, adUnitName=$adUnitName, amount=$amount, currency=$currency" }

        analyticsSystems.forEach { analyticsSystem ->
            analyticsSystem.logAdRevenue(
                format = format,
                source = source,
                adUnitName = adUnitName,
                amount = amount,
                currency = currency,
            )
        }
    }
}