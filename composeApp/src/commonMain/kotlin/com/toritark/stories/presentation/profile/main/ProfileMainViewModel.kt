package com.toritark.stories.presentation.profile.main

import co.touchlab.kermit.Logger
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

internal class ProfileMainViewModel(
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


    private companion object {
        private const val LOG_TAG = "ProfileMainViewModel"
    }
}