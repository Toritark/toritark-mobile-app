package com.toritark.app.data.learning_words.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.toritark.app.data.learning_words.db.converter.DateTimeConverters
import com.toritark.app.data.learning_words.db.dao.LearningSentencesDao
import com.toritark.app.data.learning_words.db.dao.LearningWordsDao
import com.toritark.app.data.learning_words.db.dao.WordsSentencesToLearnCrossRefDao
import com.toritark.app.data.learning_words.db.model.SentenceToLearnDbModel
import com.toritark.app.data.learning_words.db.model.WordSentenceToLearnCrossRef
import com.toritark.app.data.learning_words.db.model.WordToLearnDbModel

@Database(
    entities = [
        WordToLearnDbModel::class,
        SentenceToLearnDbModel::class,
        WordSentenceToLearnCrossRef::class,
    ],
    version = LearningWordsDatabase.VERSION,
    exportSchema = true
)
@TypeConverters(DateTimeConverters::class)
internal abstract class LearningWordsDatabase : RoomDatabase() {

    abstract fun learningWordsDao(): LearningWordsDao
    abstract fun learningSentencesDao(): LearningSentencesDao
    abstract fun wordsSentencesToLearnCrossRefDao(): WordsSentencesToLearnCrossRefDao

    companion object {
        const val VERSION = 1
        const val DB_NAME = "learning_words.db"
    }
}