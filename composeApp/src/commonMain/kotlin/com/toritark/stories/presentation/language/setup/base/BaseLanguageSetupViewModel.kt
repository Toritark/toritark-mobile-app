package com.toritark.stories.presentation.language.setup.base

import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.language.setup.base.model.BaseLanguageSetupScreenContent
import kotlinx.coroutines.CoroutineDispatcher

internal abstract class BaseLanguageSetupViewModel<C : BaseLanguageSetupScreenContent>(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
    defaultContentValue: C,
) : BaseViewModel<C>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = defaultContentValue,
) {

    abstract fun initialize()
    abstract fun onNextButtonClick()
}