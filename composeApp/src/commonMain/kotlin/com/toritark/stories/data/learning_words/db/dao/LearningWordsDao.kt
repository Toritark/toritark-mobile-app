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

//    @Query("SELECT * FROM words_to_learn WHERE id = :wordId")
//    suspend fun getWordById(wordId: Long): WordToLearnDbModel?
//
//    @Query("SELECT * FROM words_to_learn WHERE text = :text")
//    suspend fun getWordByText(text: String): WordToLearnDbModel?
//
//    @Query("SELECT * FROM words_to_learn WHERE is_learned = 0 ORDER BY last_attempt_timestamp ASC LIMIT 1")
//    suspend fun getNextWordToLearn(): WordToLearnDbModel?
//
//    // To observe changes for the next word to learn
//    @Query("SELECT * FROM words_to_learn WHERE is_learned = 0 ORDER BY last_attempt_timestamp ASC LIMIT 1")
//    fun getNextWordToLearnFlow(): Flow<WordToLearnDbModel?>
//
//    @Transaction // Ensures atomic operation
//    @Query("SELECT * FROM words_to_learn WHERE word_id = :wordId")
//    suspend fun getWordWithSentences(wordId: Long): WordWithSentences?

}