package com.toritark.stories.data.learning_words.db.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WordToLearnWithSentences(
    @Embedded val word: WordToLearnDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(WordSentenceToLearnCrossRef::class),
    )
    val sentences: List<SentenceToLearnDbModel>,
)
