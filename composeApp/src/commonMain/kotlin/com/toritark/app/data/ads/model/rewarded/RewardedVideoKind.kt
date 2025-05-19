package com.toritark.app.data.ads.model.rewarded

sealed interface RewardedVideoKind {
    data object Generation : RewardedVideoKind
    data object RetellingCheck : RewardedVideoKind
}