package com.toritark.stories.presentation.main.screen

import co.touchlab.kermit.Logger
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

internal class MainViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    private companion object {
        private const val LOG_TAG = "MainViewModel"
    }
}