@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.billing.paywall

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack

@Composable
internal expect fun PaywallScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    paywallSource: PaywallSource,
)