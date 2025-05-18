package com.toritark.app.data.profile.model

import com.toritark.app.data.profile.api.model.ProfileApiModel

sealed interface ProfileState {
    data object Unknown : ProfileState
    data object Missing : ProfileState

    data class Present(
        val profile: ProfileApiModel,
    ) : ProfileState
}