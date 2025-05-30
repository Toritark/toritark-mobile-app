package com.toritark.app.data.core_preferences

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual class Preferences {
    actual fun createSettings(name: String): Settings {
        return StorageSettings()
    }
}