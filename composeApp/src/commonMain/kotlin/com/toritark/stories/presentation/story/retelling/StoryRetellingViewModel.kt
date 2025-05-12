package com.toritark.stories.presentation.story.retelling

import co.touchlab.kermit.Logger
import com.toritark.stories.data.story.model.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class StoryRetellingViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    var story: StoryApiModel? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }

    private val _retellingText = MutableStateFlow("")
    val retellingText = _retellingText.asStateFlow()

    private val _isSubmitButtonEnabled = MutableStateFlow(false)
    val isSubmitButtonEnabled = _isSubmitButtonEnabled.asStateFlow()

    fun onTextChange(text: String) {
        logger.d { "onTextChanged: text=$text" }

        _retellingText.value = text
        _isSubmitButtonEnabled.value = text.isNotBlank()
    }

    fun onSubmitButtonClick() {
        logger.d { "onSubmitButtonClick" }

        // TODO
    }


    private companion object {
        private const val LOG_TAG = "StoryRetellingViewModel"
    }
}