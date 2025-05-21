package com.toritark.app.presentation.billing.paywall.model

internal data class PaywallScreenState(
    val dialogState: DialogState = DialogState.Hidden,
) {
    sealed interface DialogState {
        data object Hidden : DialogState

        sealed interface Visible : DialogState {
            sealed interface Checking : Visible {
                data object Normal : Checking
                data object TooLong : Checking
            }

            data object Success : Visible
            data object Fail : Visible
        }
    }
}