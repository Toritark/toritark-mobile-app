package com.toritark.app.presentation.ads.banner

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.viewinterop.AndroidView
import co.touchlab.kermit.Logger
import com.appodeal.ads.Appodeal

private const val LOG_TAG = "BannerView"
private val logger = Logger.withTag(LOG_TAG)


@Composable
actual fun BannerView(
    modifier: Modifier,
    onAdded: () -> Unit,
) {
    var lastWidth = remember(Unit) { 0 }

    AndroidView(
        factory = { context ->
            Appodeal.getBannerView(context)
        },
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                if (coordinates.size.width != lastWidth) {
                    logger.d { "Banner view positioned: ${coordinates.size}" }

                    lastWidth = coordinates.size.width
                    onAdded()
                }
            },
    )
}