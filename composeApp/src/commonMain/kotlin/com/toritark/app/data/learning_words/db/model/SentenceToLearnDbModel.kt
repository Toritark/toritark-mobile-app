package com.toritark.app.data.learning_words.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * Sentence to learn entity.
 * Language code is denormalized on purpose
 */
@Entity(
    tableName = "sentences_to_learn",
    indices = [
        Index(value = ["language_code", "learning_language_text"], unique = true),
        Index(value = ["language_code", "is_learned"], unique = false),
        Index(value = ["is_learned"], unique = false),
        Index(value = ["correct_attempts"], unique = false),
        Index(value = ["incorrect_attempts"], unique = false),
        Index(value = ["last_attempt"], unique = false),
    ]
)
data class SentenceToLearnDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "language_code")
    val languageCode: String,

    @ColumnInfo(name = "learning_language_text")
    val learningLanguageText: String,

    @ColumnInfo(name = "native_language_text")
    val nativeLanguageText: String,

    @ColumnInfo(name = "is_learned")
    val isLearned: Boolean = false,

    @ColumnInfo(name = "correct_attempts")
    val correctAttempts: Int = 0,

    @ColumnInfo(name = "incorrect_attempts")
    val incorrectAttempts: Int = 0,

    @ColumnInfo(name = "last_attempt")
    val lastAttempt: Instant = Instant.fromEpochMilliseconds(0L),
)
