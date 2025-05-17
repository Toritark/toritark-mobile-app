@file:OptIn(ExperimentalForeignApi::class)

package com.toritark.app.di.module

import androidx.room.Room
import androidx.room.RoomDatabase
import com.toritark.app.data.learning_words.db.LearningWordsDatabase
import com.toritark.app.di.name.LearningWordsNames
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

internal actual val learningWordsPlatformModule = module {

    factory<RoomDatabase.Builder<LearningWordsDatabase>>(
        named(LearningWordsNames.LEARNING_WORDS_DATABASE)
    ) {
        val dbFilePath = "${documentDirectory()}/${LearningWordsDatabase.DB_NAME}"

        Room.databaseBuilder(
            name = dbFilePath,
        )
    }
}