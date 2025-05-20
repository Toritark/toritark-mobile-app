package com.toritark.app.data.profile.model

import androidx.compose.runtime.Immutable
import com.toritark.app.data.profile.api.model.ProfileApiModel

@Immutable
sealed interface ProfileState {
    @Immutable
    data object Unknown : ProfileState

    @Immutable
    data object Missing : ProfileState

    @Immutable
    data class Present(
        val profile: ProfileApiModel,
    ) : ProfileState
}