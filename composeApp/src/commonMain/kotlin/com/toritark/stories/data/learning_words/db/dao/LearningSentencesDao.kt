package com.toritark.stories.data.learning_words.db.dao

import androidx.room.*
import com.toritark.stories.data.learning_words.db.model.SentenceToLearnDbModel
import com.toritark.stories.data.learning_words.db.model.SentenceToLearnWithWords

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

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSentence(sentence: SentenceToLearnDbModel)

    @Query("SELECT * FROM sentences_to_learn WHERE is_learned = 0 AND language_code = :languageCode ORDER BY last_attempt ASC LIMIT 1")
    suspend fun getNextSentenceToLearn(languageCode: String): SentenceToLearnWithWords?
}