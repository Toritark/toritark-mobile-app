package com.toritark.app.data.billing.api.repository

import com.toritark.app.data.billing.api.resource.BillingApiResources
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface BillingApiRepository {
    suspend fun triggerPlanCheck()
}

internal class BillingApiRepositoryImpl(
    private val httpClient: HttpClient,
    private val ioDispatcher: CoroutineDispatcher,
) : BillingApiRepository {

    override suspend fun triggerPlanCheck() {
        withContext(ioDispatcher) {
            httpClient.get(BillingApiResources.Plans.Check.Trigger()) {
                expectSuccess = true
            }
        }
    }
}