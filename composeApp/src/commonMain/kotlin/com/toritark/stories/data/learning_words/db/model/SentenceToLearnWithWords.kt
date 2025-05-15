package com.toritark.stories.data.learning_words.db.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SentenceToLearnWithWords(
    @Embedded val sentence: SentenceToLearnDbModel,
    @Relation(
        parentColumn = "sentence_id",
        entityColumn = "word_id",
        associateBy = Junction(WordSentenceToLearnCrossRef::class),
    )
    val words: List<WordToLearnDbModel>,
)
