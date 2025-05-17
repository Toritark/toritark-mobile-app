package com.toritark.app.presentation.core_ui.clipboard

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryOf(text: String): ClipEntry {
    return ClipEntry(
        ClipData(
            "text",
            arrayOf("text/plain"),
            ClipData.Item(text)
        )
    )
}