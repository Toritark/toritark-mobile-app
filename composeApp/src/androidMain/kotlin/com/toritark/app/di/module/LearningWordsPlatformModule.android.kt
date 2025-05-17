package com.toritark.app.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.toritark.app.data.learning_words.db.LearningWordsDatabase
import com.toritark.app.di.name.LearningWordsNames
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val learningWordsPlatformModule = module {

    factory<RoomDatabase.Builder<LearningWordsDatabase>>(
        named(LearningWordsNames.LEARNING_WORDS_DATABASE)
    ) {
        val context: Context = get()
        val dbFilePath = context.getDatabasePath(LearningWordsDatabase.DB_NAME).absolutePath

        Room.databaseBuilder(
            context = context,
            name = dbFilePath,
        )
    }
}