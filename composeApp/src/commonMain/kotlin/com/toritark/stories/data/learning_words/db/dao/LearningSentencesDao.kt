package com.toritark.stories.data.learning_words.db.dao

import androidx.room.*
import com.toritark.stories.data.learning_words.db.model.SentenceToLearnDbModel

@Dao
internal interface LearningSentencesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSentences(sentences: List<SentenceToLearnDbModel>): LongArray

    @Query("SELECT * FROM sentences_to_learn WHERE language_code = :languageCode AND learning_language_text IN (:learningLanguageSentences)")
    suspend fun getExistingSentences(
        languageCode: String,
        learningLanguageSentences: Collection<String>,
    ): List<SentenceToLearnDbModel>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSentences(sentences: Collection<SentenceToLearnDbModel>)
}