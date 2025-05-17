@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.data.core_preferences

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual class Preferences {

    actual fun createSettings(name: String): Settings {
        val delegate = NSUserDefaults(suiteName = name)
        return NSUserDefaultsSettings(delegate)
    }
}