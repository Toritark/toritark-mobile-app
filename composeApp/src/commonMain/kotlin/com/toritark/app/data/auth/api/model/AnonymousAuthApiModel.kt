package com.toritark.app.data.auth.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnonymousAuthApiModel(
    @SerialName("language")
    val language: String,
)