package com.toritark.stories.presentation.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FlatCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    cornerRadius: Dp,
    rightIcon: ImageVector? = null,
    rightIconTint: Color = LocalContentColor.current,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit,
) {
    val shape = RoundedCornerShape(size = cornerRadius)

    val hapticFeedback = LocalHapticFeedback.current

    Row(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = shape,
            )
            .clip(shape)
            .clickable {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
            }
            .padding(horizontal = 8.dp, vertical = 24.dp),
    ) {
        content()

        rightIcon?.let { icon ->
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(32.dp),
                imageVector = icon,
                contentDescription = null,
                tint = rightIconTint,
            )
        }
    }
}

@Preview
@Composable
fun FlatCardPreview() {
    FlatCard(
        modifier = Modifier,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        cornerRadius = 24.dp,
        rightIcon = Icons.Default.ChevronRight,
        rightIconTint = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Column(
            modifier = Modifier
                .size(width = 200.dp, height = 400.dp)
                .padding(16.dp),
        ) {
            Text(
                text = "Hello, World!",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}