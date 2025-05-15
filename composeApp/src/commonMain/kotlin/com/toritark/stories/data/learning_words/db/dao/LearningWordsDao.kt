package com.toritark.stories.data.learning_words.db.dao

import androidx.room.*
import com.toritark.stories.data.learning_words.db.model.WordToLearnDbModel

@Dao
internal interface LearningWordsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWords(words: List<WordToLearnDbModel>): LongArray

    @Query("SELECT * FROM words_to_learn WHERE language_code = :languageCode AND text IN (:words)")
    suspend fun getExistingWords(languageCode: String, words: Collection<String>): List<WordToLearnDbModel>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateWords(words: Collection<WordToLearnDbModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateWord(word: WordToLearnDbModel)

    @Query("SELECT COUNT(id) FROM words_to_learn WHERE is_learned = 0 AND language_code = :languageCode")
    suspend fun getWordsToLearnCount(languageCode: String): Long

    @Transaction
    @Query("SELECT COUNT(id) FROM words_to_learn WHERE language_code = :languageCode")
    suspend fun getTotalWordsCount(languageCode: String): Long
}