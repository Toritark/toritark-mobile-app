package com.toritark.stories.data.language.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LanguageLevel(
    val value: String,
) {
    @SerialName("a1")
    A1("a1"),

    @SerialName("a2")
    A2("a2"),

    @SerialName("b1")
    B1("b1"),

    @SerialName("b2")
    B2("b2"),

    @SerialName("c1")
    C1("c1"),

    @SerialName("c2")
    C2("c2");

    companion object {
        
        fun fromValue(value: String): LanguageLevel {
            return entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Unknown value: '$value'")
        }
    }
}