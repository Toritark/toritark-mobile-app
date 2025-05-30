package com.toritark.app.domain.core.language

@JsFun("() => navigator.language")
private external fun getBrowserLanguage(): String

internal actual class GetDeviceLanguageCodeImpl : GetDeviceLanguageCode {

    actual override suspend operator fun invoke(): String {
        val language = getBrowserLanguage()

        // Extract the language code (first 2 or 3 characters before the dash if present)
        return if (language.contains("-")) {
            language.split("-")[0].lowercase()
        } else {
            language.lowercase()
        }
    }
}