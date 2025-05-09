package com.toritark.stories.di.module

import com.toritark.stories.data.core_preferences.Preferences
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.data.language.repository.LanguagesRepositoryImpl
import com.toritark.stories.di.name.DispatchersNames
import com.toritark.stories.di.name.HttpClientNames
import com.toritark.stories.di.name.LanguageSettingsNames
import com.toritark.stories.presentation.language.setup.learning.LearningLanguageChooserViewModel
import com.toritark.stories.presentation.language.setup.level.LanguageLevelChooserViewModel
import com.toritark.stories.presentation.language.setup.native.NativeLanguageChooserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val languageModule = module {

    single(named(LanguageSettingsNames.LANGUAGE)) {
        val preferences: Preferences = get()
        preferences.createSettings("language")
    }

    single<LanguagesRepository> {
        LanguagesRepositoryImpl(
            httpClient = get(named(HttpClientNames.DEFAULT)),
            settings = get(named(LanguageSettingsNames.LANGUAGE)),
            ioDispatcher = get(named(DispatchersNames.IO)),
        )
    }

    viewModel<LearningLanguageChooserViewModel> {
        LearningLanguageChooserViewModel(
            languagesRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }

    viewModel {
        NativeLanguageChooserViewModel(
            getDeviceLanguageCode = get(),
            languagesRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }

    viewModel {
        LanguageLevelChooserViewModel(
            languagesRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}