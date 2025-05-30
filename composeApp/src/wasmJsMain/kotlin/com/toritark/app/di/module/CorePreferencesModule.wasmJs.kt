package com.toritark.app.di.module

import com.toritark.app.data.core_preferences.Preferences
import org.koin.dsl.module

internal actual val platformPreferencesModule = module {
    single { Preferences() }
}