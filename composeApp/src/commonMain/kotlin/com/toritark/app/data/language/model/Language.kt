package com.toritark.app.data.language.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Language(
    @SerialName("iso_code")
    val isoCode: String,
    @SerialName("name_en")
    val nameEn: String,
    @SerialName("name")
    val name: String,
    @SerialName("flag_unicode")
    val flagUnicode: String,
)
