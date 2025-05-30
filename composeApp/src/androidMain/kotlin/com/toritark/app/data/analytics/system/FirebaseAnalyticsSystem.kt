package com.toritark.app.data.analytics.system

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.toritark.app.data.analytics.extension.bundleParameters
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty

internal class FirebaseAnalyticsSystem : AnalyticsSystem(name = "Firebase") {

    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }

    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setPropertyInternal(property: AnalyticsProperty) {
        firebaseAnalytics.setUserProperty(
            property.name,
            property.value?.toString(),
        )
    }

    override fun logEventInternal(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.name, event.bundleParameters)
    }

    override fun logScreenViewInternal(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        })
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