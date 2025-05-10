package com.toritark.stories.di.module

import com.toritark.stories.data.story.repository.StoryRequestsApiRepository
import com.toritark.stories.data.story.repository.StoryRequestsApiRepositoryImpl
import com.toritark.stories.di.name.DispatchersNames
import com.toritark.stories.di.name.HttpClientNames
import com.toritark.stories.domain.story.interactor.StoriesInteractor
import com.toritark.stories.domain.story.interactor.StoriesInteractorImpl
import com.toritark.stories.presentation.story.detail.StoryDetailViewModel
import com.toritark.stories.presentation.story.text.StoryTextViewModel
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
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }

    viewModel {
        StoryTextViewModel(
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}