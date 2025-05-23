package com.toritark.app.data.analytics.model

data class AnalyticsEvent(
    val name: String,
    val parameters: Map<String, Any?> = emptyMap(),
)