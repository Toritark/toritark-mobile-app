package com.toritark.app.data.learning_words.api.resource

import com.toritark.app.data.core_api.base.resource.BaseApiResource
import io.ktor.resources.*
import kotlinx.serialization.SerialName

@Resource("learn/words/")
internal class LearningWordsApiResources(val parent: BaseApiResource = BaseApiResource()) {

    @Resource("stats/")
    class Stats(
        val parent: LearningWordsApiResources = LearningWordsApiResources(),
        @SerialName("learning_language")
        val learningLanguageCode: String,
        @SerialName("native_language")
        val nativeLanguageCode: String,
    )

    @Resource("sentences/")
    class Sentences(val parent: LearningWordsApiResources = LearningWordsApiResources()) {

        @Resource("next/")
        class Next(
            val parent: Sentences = Sentences(),
            @SerialName("learning_language")
            val learningLanguageCode: String,
            @SerialName("native_language")
            val nativeLanguageCode: String,
        )

        @Resource("add/")
        class Add(val parent: Sentences = Sentences())

        @Resource("{id}/")
        class Sentence(
            val parent: Sentences = Sentences(),
            @SerialName("id")
            val id: Long,
        ) {
            @Resource("results/")
            class Results(val parent: Sentence) {

                @Resource("update/")
                class Update(val parent: Results)
            }

            @Resource("learned/")
            class Learned(val parent: Sentence)
        }
    }
}