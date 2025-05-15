package com.toritark.stories.data.learning_words.db.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SentenceToLearnWithWords(
    @Embedded val sentence: SentenceToLearnDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = WordSentenceToLearnCrossRef::class,
            parentColumn = "sentence_id",
            entityColumn = "word_id"
        ),
    )
    val words: List<WordToLearnDbModel>,
)
