@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.stories.data.core_preferences

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual class Preferences(
    private val context: Context,
) {

    actual fun createSettings(name: String): Settings {
        return context
            .getSharedPreferences(name, Context.MODE_PRIVATE)
            .let(::SharedPreferencesSettings)
    }
}