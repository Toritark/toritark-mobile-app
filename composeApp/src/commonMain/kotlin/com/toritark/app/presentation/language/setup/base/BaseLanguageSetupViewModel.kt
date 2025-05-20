package com.toritark.app.presentation.language.setup.base

import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.language.setup.base.model.BaseLanguageSetupScreenState
import kotlinx.coroutines.CoroutineDispatcher

internal abstract class BaseLanguageSetupViewModel<C : BaseLanguageSetupScreenState>(
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