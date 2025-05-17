@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.core.language

import java.util.*

internal actual class GetDeviceLanguageCodeImpl actual constructor() : GetDeviceLanguageCode {

    actual override suspend operator fun invoke(): String {
        return Locale.getDefault().language
    }
}