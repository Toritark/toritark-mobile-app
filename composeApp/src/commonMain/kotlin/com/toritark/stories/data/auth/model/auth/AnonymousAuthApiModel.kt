package com.toritark.stories.data.auth.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnonymousAuthApiModel(
    @SerialName("language")
    val language: String,
)
