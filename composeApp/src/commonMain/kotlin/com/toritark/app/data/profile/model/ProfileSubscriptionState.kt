package com.toritark.app.data.profile.model

sealed interface ProfileSubscriptionState {
    data object Unknown : ProfileSubscriptionState
    data object Free : ProfileSubscriptionState
    data object Paid : ProfileSubscriptionState
}