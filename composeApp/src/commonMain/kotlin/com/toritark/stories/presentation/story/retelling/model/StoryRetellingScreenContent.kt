package com.toritark.stories.presentation.story.retelling.model

import com.toritark.stories.data.story.model.story.StoryApiModel

internal data class StoryRetellingScreenContent(
    val story: StoryApiModel? = null,
    val retellingText: String = "",
    val isSubmitButtonEnabled: Boolean = false,
    val checkResult: CheckResult = CheckResult.None,
) {

    interface CheckResult {
        data object None : CheckResult
        data object Checking : CheckResult
        // TODO
    }
}
