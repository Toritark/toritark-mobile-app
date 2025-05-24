package com.toritark.app.data.analytics.extension

import androidx.core.bundle.Bundle
import com.toritark.app.data.analytics.model.AnalyticsEvent

internal val AnalyticsEvent.bundleParameters: Bundle
    get() = Bundle().apply {
        parameters.forEach { (key, value) ->
            when (value) {
                null -> putString(key, null)
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Double -> putDouble(key, value)
                is Long -> putLong(key, value)
            }
        }
    }