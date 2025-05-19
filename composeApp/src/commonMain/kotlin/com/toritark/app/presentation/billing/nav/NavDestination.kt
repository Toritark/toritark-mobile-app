package com.toritark.app.presentation.billing.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.toritark.app.presentation.billing.paywall.PaywallScreen
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.nav.NavDestination
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.nav.navTypeOf
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
sealed interface BillingNavDestination : NavDestination {

    @Serializable
    data class Paywall(
        val source: PaywallSource,
    ) : BillingNavDestination
}

fun NavGraphBuilder.billingScreens(onNavigateTo: OnNavigateTo, onPopBackStack: OnPopBackStack) {

    composable<BillingNavDestination.Paywall>(
        typeMap = mapOf(
            typeOf<PaywallSource>() to navTypeOf<PaywallSource>(),
        )
    ) {
        val route = it.toRoute<BillingNavDestination.Paywall>()

        PaywallScreen(
            onNavigateTo = onNavigateTo,
            onPopBackStack = onPopBackStack,
            paywallSource = route.source,
        )
    }
}
