package com.toritark.stories.di.module

import com.toritark.stories.data.core_preferences.Preferences
import org.koin.dsl.module

internal actual val platformPreferencesModule = module {
    single { Preferences(get()) }
}
