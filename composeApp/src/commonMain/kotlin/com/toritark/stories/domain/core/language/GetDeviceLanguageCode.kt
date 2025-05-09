package com.toritark.stories.domain.core.language

interface GetDeviceLanguageCode {
    suspend operator fun invoke(): String
}

internal expect class GetDeviceLanguageCodeImpl() : GetDeviceLanguageCode {
    override suspend operator fun invoke(): String
}