package com.toritark.stories.presentation.core_ui.icon

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.MagicChange: ImageVector
    get() {
        magicChange?.let { return it }

        return ImageVector
            .Builder(
                name = "Low_priority",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f
            )
            .apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(160f, 459f)
                    quadToRelative(0f, 71f, 47.5f, 122f)
                    reflectiveQuadTo(326f, 638f)
                    lineToRelative(-62f, -62f)
                    lineToRelative(56f, -56f)
                    lineToRelative(160f, 160f)
                    lineToRelative(-160f, 160f)
                    lineToRelative(-56f, -56f)
                    lineToRelative(64f, -64f)
                    quadToRelative(-105f, -6f, -176.5f, -81f)
                    reflectiveQuadTo(80f, 460f)
                    quadToRelative(0f, -109f, 75.5f, -184.5f)
                    reflectiveQuadTo(340f, 200f)
                    horizontalLineToRelative(140f)
                    verticalLineToRelative(80f)
                    horizontalLineTo(340f)
                    quadToRelative(-75f, 0f, -127.5f, 52f)
                    reflectiveQuadTo(160f, 459f)
                    moveToRelative(400f, 261f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(320f)
                    verticalLineToRelative(80f)
                    close()
                    moveToRelative(0f, -220f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(320f)
                    verticalLineToRelative(80f)
                    close()
                    moveToRelative(0f, -220f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(320f)
                    verticalLineToRelative(80f)
                    close()
                }
            }
            .build()
            .also { magicChange = it }
    }

private var magicChange: ImageVector? = null