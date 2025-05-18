package com.toritark.app.data.profile.api.model

import com.toritark.app.data.billing.api.model.PlanApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileApiModel(
    @SerialName("id")
    val id: Long,
    @SerialName("is_anonymous")
    val isAnonymous: Boolean,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("avatar")
    val avatarUrl: String?,
    @SerialName("email")
    val email: String,
    @SerialName("plan")
    val plan: PlanApiModel,
)
