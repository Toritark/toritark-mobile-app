package com.toritark.stories.presentation.splash

import co.touchlab.kermit.Logger
import com.toritark.stories.domain.auth.interactor.AuthInteractor
import com.toritark.stories.domain.auth.model.AuthState
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authInteractor: AuthInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val coroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    init {
        initialize()
    }

    private fun initialize() {
        coroutineScope.launch {
            authInteractor.authState.collect { state ->
                logger.d { "initialize: authState=$state" }

                when (state) {
                    is AuthState.Authenticated -> {
                        // TODO: Navigate
                    }

                    else -> {}
                }
            }
        }
    }


    private companion object {
        private const val LOG_TAG = "SplashViewModel"
    }
}