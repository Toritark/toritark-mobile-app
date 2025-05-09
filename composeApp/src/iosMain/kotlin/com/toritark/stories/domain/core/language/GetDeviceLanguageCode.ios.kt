@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.stories.domain.core.language

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

internal actual class GetDeviceLanguageCodeImpl actual constructor() : GetDeviceLanguageCode {

    actual override suspend fun invoke(): String {
        return NSLocale.currentLocale().languageCode
    }
}