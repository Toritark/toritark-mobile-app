package com.toritark.app.presentation.story.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class QuotaExceededMessage(
    val title: String,
    val text: String,
    val canShowRewardedAd: Boolean,
)