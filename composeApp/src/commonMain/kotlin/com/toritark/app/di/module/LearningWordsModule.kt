package com.toritark.app.di.module

import com.toritark.app.data.learning_words.api.repository.LearningWordsApiRepository
import com.toritark.app.data.learning_words.api.repository.LearningWordsApiRepositoryImpl
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.HttpClientNames
import com.toritark.app.domain.learning_words.interactor.LearningWordsInteractor
import com.toritark.app.domain.learning_words.interactor.LearningWordsInteractorImpl
import com.toritark.app.presentation.learning_words.main.LearningWordsMainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val learningWordsModule = module {
    single<LearningWordsApiRepository> {
        LearningWordsApiRepositoryImpl(
            httpClient = get(named(HttpClientNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }

    single<LearningWordsInteractor> {
        LearningWordsInteractorImpl(
            languagesRepository = get(),
            learningWordsApiRepository = get(),
        )
    }

    viewModel {
        LearningWordsMainViewModel(
            learningWordsInteractor = get(),
            adsInteractor = get(),
            billingInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}