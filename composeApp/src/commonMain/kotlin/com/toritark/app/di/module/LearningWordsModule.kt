package com.toritark.app.di.module

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.toritark.app.data.learning_words.db.LearningWordsDatabase
import com.toritark.app.data.learning_words.repository.LearningWordsRepository
import com.toritark.app.data.learning_words.repository.LearningWordsRepositoryImpl
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.LearningWordsNames
import com.toritark.app.domain.learning_words.interactor.LearningWordsInteractor
import com.toritark.app.domain.learning_words.interactor.LearningWordsInteractorImpl
import com.toritark.app.presentation.learning_words.main.LearningWordsMainViewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val learningWordsModule = module {
    includes(learningWordsPlatformModule)

    single {
        val dbBuilder: RoomDatabase.Builder<LearningWordsDatabase> = get(
            named(LearningWordsNames.LEARNING_WORDS_DATABASE)
        )

        val ioDispatcher: CoroutineDispatcher = get(named(DispatchersNames.IO))

        dbBuilder
//            .addMigrations(MIGRATIONS)
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .fallbackToDestructiveMigration(dropAllTables = true) // FIXME: Remove on release
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(ioDispatcher)
            .build()
    }

    single {
        val db: LearningWordsDatabase = get()
        db.learningWordsDao()
    }

    single {
        val db: LearningWordsDatabase = get()
        db.learningSentencesDao()
    }

    single {
        val db: LearningWordsDatabase = get()
        db.wordsSentencesToLearnCrossRefDao()
    }

    single<LearningWordsRepository> {
        LearningWordsRepositoryImpl(
            learningWordsDao = get(),
            learningSentencesDao = get(),
            wordsSentencesToLearnCrossRefDao = get(),
            ioDispatcher = get(named(DispatchersNames.IO)),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }

    single<LearningWordsInteractor> {
        LearningWordsInteractorImpl(
            languagesRepository = get(),
            learningWordsRepository = get(),
        )
    }

    viewModel {
        LearningWordsMainViewModel(
            learningWordsInteractor = get(),
            adsInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}