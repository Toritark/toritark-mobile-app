package com.toritark.stories.data.word.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * Word to learn entity.
 * Language code is denormalized on purpose
 */
@Entity(
    tableName = "words_to_learn",
    indices = [
        Index(value = ["language_code", "text"], unique = true),
        Index(value = ["language_code", "is_learned"], unique = false),
        Index(value = ["is_learned"], unique = false),
        Index(value = ["languageCode"], unique = false),
        Index(value = ["correctAttempts"], unique = false),
        Index(value = ["incorrectAttempts"], unique = false),
        Index(value = ["lastAttempt"], unique = false),
    ],
)
data class WordToLearnDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "language_code")
    val languageCode: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "is_learned")
    val isLearned: Boolean = false,

    @ColumnInfo(name = "correct_attempts")
    val correctAttempts: Int = 0,

    @ColumnInfo(name = "incorrect_attempts")
    val incorrectAttempts: Int = 0,

    @ColumnInfo(name = "last_attempt")
    val lastAttempt: Instant = Instant.fromEpochMilliseconds(0L)
)
