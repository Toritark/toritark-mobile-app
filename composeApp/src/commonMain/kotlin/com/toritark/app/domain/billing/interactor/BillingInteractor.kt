package com.toritark.app.domain.billing.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.domain.billing.provider.BillingProvider
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface BillingInteractor {
    fun initialize()
}

internal class BillingInteractorImpl(
    private val billingProvider: BillingProvider,
    private val profileInteractor: ProfileInteractor,
    private val defaultDispatcher: CoroutineDispatcher,
) : BillingInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    private val coroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    override fun initialize() {
        logger.d { "initialize" }

        coroutineScope.launch {
            profileInteractor
                .presentProfileState
                .map { profileState -> profileState.profile }
                .collect { profile ->
                    logger.d { "initialize: profile=$profile" }

                    billingProvider.initialize(userId = profile.id)
                }
        }
    }

    private companion object {
        private const val LOG_TAG = "BillingInteractor"
    }
}