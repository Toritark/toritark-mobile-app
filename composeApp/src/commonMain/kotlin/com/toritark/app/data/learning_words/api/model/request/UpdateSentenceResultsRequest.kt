package com.toritark.app.data.learning_words.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateSentenceResultsRequest(
    @SerialName("correct_words_ids")
    val correctWordsIds: Collection<Long>,
    @SerialName("incorrect_words_ids")
    val incorrectWordsIds: Collection<Long>,
)
