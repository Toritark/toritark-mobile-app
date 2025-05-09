@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.stories.data.core_preferences

import com.russhwolf.settings.Settings

expect class Preferences {
    fun createSettings(name: String): Settings
}