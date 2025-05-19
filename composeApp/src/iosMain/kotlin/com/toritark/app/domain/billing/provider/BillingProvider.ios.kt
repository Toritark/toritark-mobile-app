package com.toritark.app.domain.billing.provider

import org.jetbrains.compose.resources.getString
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.revenuecat_ios_api_key

internal actual suspend fun getRevenueCatApiKey(): String {
    return getString(Res.string.revenuecat_ios_api_key)
}