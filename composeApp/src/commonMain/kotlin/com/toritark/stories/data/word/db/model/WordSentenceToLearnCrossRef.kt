package com.toritark.stories.data.word.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "word_sentence_cross_ref",
    primaryKeys = ["word_id", "sentence_id"],
    foreignKeys = [
        ForeignKey(
            entity = WordToLearnDbModel::class,
            parentColumns = ["word_id"],
            childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SentenceToLearnDbModel::class,
            parentColumns = ["sentence_id"],
            childColumns = ["sentence_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["word_id"]),
        Index(value = ["sentence_id"]),
        Index(value = ["word_id", "sentence_id"], unique = true),
    ]
)
data class WordSentenceToLearnCrossRef(
    @ColumnInfo(name = "word_id")
    val wordId: Long,
    @ColumnInfo(name = "sentence_id")
    val sentenceId: Long,
)
