@file:OptIn(ExperimentalComposeUiApi::class)

package com.toritark.app.presentation.core_ui.clipboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryOf(text: String): ClipEntry {
    return ClipEntry.withPlainText(text)
}