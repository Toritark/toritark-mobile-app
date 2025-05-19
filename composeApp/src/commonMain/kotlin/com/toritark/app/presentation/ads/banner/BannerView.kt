package com.toritark.app.presentation.ads.banner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun BannerView(
    modifier: Modifier = Modifier,
    onAdded: () -> Unit,
)