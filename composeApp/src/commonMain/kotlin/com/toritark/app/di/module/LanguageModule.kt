package com.toritark.app.di.module

import com.toritark.app.data.core_preferences.Preferences
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.data.language.repository.LanguagesRepositoryImpl
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.HttpClientNames
import com.toritark.app.di.name.LanguageSettingsNames
import com.toritark.app.presentation.language.setup.learning.LearningLanguageChooserViewModel
import com.toritark.app.presentation.language.setup.level.LanguageLevelChooserViewModel
import com.toritark.app.presentation.language.setup.native.NativeLanguageChooserViewModel
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