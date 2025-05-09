package com.toritark.stories.presentation.language.setup.base

import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal abstract class BaseLanguageSetupViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {

    protected val _isNextButtonEnabled = MutableStateFlow(false)
    val isNextButtonEnabled = _isNextButtonEnabled.asStateFlow()

    abstract fun initialize()
    abstract fun onNextButtonClick()
}