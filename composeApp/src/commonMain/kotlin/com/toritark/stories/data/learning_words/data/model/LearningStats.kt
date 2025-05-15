package com.toritark.stories.data.learning_words.data.model

data class LearningStats(
    val words: Words,
) {
    val isEmpty = words.isEmpty

    data class Words(
        val learned: Long,
        val toLearn: Long,
        val total: Long,
    ) {
        val isEmpty = total == 0L
    }
}
