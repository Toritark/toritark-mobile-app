package com.toritark.app.presentation.main.screen

import co.touchlab.kermit.Logger
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.presentation.billing.nav.BillingNavDestination
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

internal class MainViewModel(
    private val profileInteractor: ProfileInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<Unit>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = Unit,
) {
    override val logger = Logger.withTag(LOG_TAG)

    val profileState = profileInteractor.profileState

    fun onTopBarUpgradeClick() {
        logger.d { "onTopBarUpgradeClick" }

        onNavigateTo(BillingNavDestination.Paywall(source = PaywallSource.TopBar)) {}
    }

    private companion object {
        private const val LOG_TAG = "MainViewModel"
    }
}