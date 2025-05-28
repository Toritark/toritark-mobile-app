package com.toritark.app.data.analytics.system

import cocoapods.FirebaseAnalytics.*
import cocoapods.FirebaseCore.FIRApp
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty

internal class FirebaseAnalyticsSystem : AnalyticsSystem(name = "Firebase") {

    override fun initialize() {
        FIRApp.configure()
    }

    override fun setUserId(userId: String) {
        FIRAnalytics.setUserID(userId)
    }

    override fun setPropertyInternal(property: AnalyticsProperty) {
        FIRAnalytics.setUserPropertyString(
            value = property.value?.toString(),
            forName = property.name,
        )
    }

    override fun logEventInternal(event: AnalyticsEvent) {
        FIRAnalytics.logEventWithName(
            name = event.name,
            parameters = event.parameters.toMap(),
        )
    }

    override fun logScreenViewInternal(screenName: String) {
        FIRAnalytics.logEventWithName(
            name = kFIREventScreenView.orEmpty(),
            parameters = mapOf(
                kFIRParameterScreenName to screenName,
                kFIRParameterScreenClass to screenName,
            ),
        )
    }

    override fun logAdRevenue(
        format: String,
        source: String,
        adUnitName: String,
        amount: Double,
        currency: String,
    ) {
        FIRAnalytics.logEventWithName(
            name = requireNotNull(kFIREventAdImpression),
            parameters = mapOf(
                kFIRParameterAdFormat to format,
                kFIRParameterAdSource to source,
                kFIRParameterAdUnitName to adUnitName,
                kFIRParameterValue to amount,
                kFIRParameterCurrency to currency,
            )
        )
    }
}