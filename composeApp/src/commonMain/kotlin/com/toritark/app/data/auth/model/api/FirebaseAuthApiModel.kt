package com.toritark.app.data.auth.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FirebaseAuthApiModel(
    @SerialName("token")
    val token: String,
    @SerialName("language")
    val language: String,
)