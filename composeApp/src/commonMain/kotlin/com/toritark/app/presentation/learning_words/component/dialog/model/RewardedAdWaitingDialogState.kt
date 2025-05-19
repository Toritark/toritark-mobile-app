package com.toritark.app.presentation.learning_words.component.dialog.model

sealed interface RewardedAdWaitingDialogState {
    data object None : RewardedAdWaitingDialogState
    data object Waiting : RewardedAdWaitingDialogState
    data object Fail : RewardedAdWaitingDialogState
    data object Success : RewardedAdWaitingDialogState
}