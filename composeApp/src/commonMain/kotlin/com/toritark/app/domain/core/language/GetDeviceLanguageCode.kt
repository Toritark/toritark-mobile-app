@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.core.language

interface GetDeviceLanguageCode {
    suspend operator fun invoke(): String
}

internal expect class GetDeviceLanguageCodeImpl : GetDeviceLanguageCode {
    override suspend operator fun invoke(): String
}