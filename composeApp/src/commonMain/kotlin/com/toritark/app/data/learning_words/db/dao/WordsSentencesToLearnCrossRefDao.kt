package com.toritark.app.data.learning_words.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.toritark.app.data.learning_words.db.model.WordSentenceToLearnCrossRef

@Dao
internal interface WordsSentencesToLearnCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRefs: Collection<WordSentenceToLearnCrossRef>)
}