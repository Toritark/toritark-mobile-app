package com.toritark.stories.data.word.db.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WordToLearnWithSentences(
    @Embedded val word: WordToLearnDbModel,
    @Relation(
        parentColumn = "word_id",
        entityColumn = "sentence_id",
        associateBy = Junction(WordSentenceToLearnCrossRef::class),
    )
    val sentences: List<SentenceToLearnDbModel>,
)
