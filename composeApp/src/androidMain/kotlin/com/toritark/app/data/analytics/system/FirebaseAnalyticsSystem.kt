package com.toritark.app.data.analytics.system

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty

class FirebaseAnalyticsSystem : AnalyticsSystem(name = "Firebase") {

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
        val paramsBundle = Bundle().apply {
            event.parameters.forEach { (key, value) ->
                when (value) {
                    null -> putString(key, null)
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Float -> putFloat(key, value)
                    is Double -> putDouble(key, value)
                    is Long -> putLong(key, value)
                    else -> {
                        logger.d { "Unknown event type: $key=$value" }
                    }
                }
            }
        }

        firebaseAnalytics.logEvent(event.name, paramsBundle)
    }

    override fun logScreenViewInternal(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        })
    }
}