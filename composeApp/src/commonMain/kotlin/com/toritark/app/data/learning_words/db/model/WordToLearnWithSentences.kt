package com.toritark.app.data.learning_words.db.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WordToLearnWithSentences(
    @Embedded val word: WordToLearnDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = WordSentenceToLearnCrossRef::class,
            parentColumn = "word_id",
            entityColumn = "sentence_id"
        ),
    )
    val sentences: List<SentenceToLearnDbModel>,
)
