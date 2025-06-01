package com.toritark.app.presentation.ads.banner

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.viewinterop.UIKitView
import co.touchlab.kermit.Logger
import cocoapods.Google_Mobile_Ads_SDK.GADBannerView
import cocoapods.Google_Mobile_Ads_SDK.GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView

private const val LOG_TAG = "BannerView"
private val logger = Logger.withTag(LOG_TAG)

@Composable
actual fun BannerView(
    modifier: Modifier,
    onAdded: () -> Unit,
) {
    var lastWidth = remember(Unit) { 0 }
    var lastBannerView = remember<UIView?>(Unit) { null }

    val viewController = LocalUIViewController.current

    UIKitView(
        factory = {
            logger.i { "Creating banner view, current width = $lastWidth" }

            val size = GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth(
                width = viewController.view.frame.useContents { size.width },
            )

            val bannerView = GADBannerView(
               adSize = size,
            )
//            bannerView.translatesAutoresizingMaskIntoConstraints = false

            lastBannerView = bannerView

            bannerView
        },
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                if (coordinates.size.width != lastWidth) {
                    logger.i { "Banner view positioned: ${coordinates.size}" }

                    lastWidth = coordinates.size.width

//                    lastBannerView?.setFrame(
//                        CGRectMake(
//                            x = 0.0,
//                            y = 0.0,
//                            width = lastWidth.toDouble(),
//                            height = 50.0,
//                        )
//                    )

                    onAdded()
                }
            },
    )
}