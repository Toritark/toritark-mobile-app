package com.toritark.app.di.module

import com.toritark.app.data.story.repository.StoryRequestsApiRepository
import com.toritark.app.data.story.repository.StoryRequestsApiRepositoryImpl
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.HttpClientNames
import com.toritark.app.domain.story.interactor.StoriesInteractor
import com.toritark.app.domain.story.interactor.StoriesInteractorImpl
import com.toritark.app.presentation.story.detail.StoryDetailViewModel
import com.toritark.app.presentation.story.quiz.StoryQuizViewModel
import com.toritark.app.presentation.story.retelling.detail.StoryRetellingDetailViewModel
import com.toritark.app.presentation.story.retelling.section.StoryRetellingViewModel
import com.toritark.app.presentation.story.text.StoryTextViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val storyModule = module {

    single<StoryRequestsApiRepository> {
        StoryRequestsApiRepositoryImpl(
            httpClient = get(named(HttpClientNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
        )
    }

    single<StoriesInteractor> {
        StoriesInteractorImpl(
            storyRequestsApiRepository = get(),
            languagesRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
        )
    }

    viewModel {
        StoryDetailViewModel(
            storiesInteractor = get(),
            profileInteractor = get(),
            adsInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }

    viewModel {
        StoryTextViewModel(
            learningWordsInteractor = get(),
            adsInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }

    viewModel {
        StoryQuizViewModel(
            adsInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }

    viewModel {
        StoryRetellingViewModel(
            storiesInteractor = get(),
            profileInteractor = get(),
            adsInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }

    viewModel {
        StoryRetellingDetailViewModel(
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}