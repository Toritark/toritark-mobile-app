package com.toritark.stories.presentation.core_ui.icon

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AppIcons

val AppIcons.Customize: ImageVector
    get() {
        customize?.let { return it }

        return ImageVector
            .Builder(
                name = "AdjustmentsHorizontal",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            )
            .apply {
                path(
                    fill = null,
                    fillAlpha = 1.0f,
                    stroke = SolidColor(Color(0xFF0F172A)),
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.5f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(10.5f, 6f)
                    lineTo(20.25f, 6f)
                    moveTo(10.5f, 6f)
                    curveTo(10.5f, 6.8284f, 9.8284f, 7.5f, 9f, 7.5f)
                    curveTo(8.1716f, 7.5f, 7.5f, 6.8284f, 7.5f, 6f)
                    moveTo(10.5f, 6f)
                    curveTo(10.5f, 5.1716f, 9.8284f, 4.5f, 9f, 4.5f)
                    curveTo(8.1716f, 4.5f, 7.5f, 5.1716f, 7.5f, 6f)
                    moveTo(3.75f, 6f)
                    horizontalLineTo(7.5f)
                    moveTo(10.5f, 18f)
                    horizontalLineTo(20.25f)
                    moveTo(10.5f, 18f)
                    curveTo(10.5f, 18.8284f, 9.8284f, 19.5f, 9f, 19.5f)
                    curveTo(8.1716f, 19.5f, 7.5f, 18.8284f, 7.5f, 18f)
                    moveTo(10.5f, 18f)
                    curveTo(10.5f, 17.1716f, 9.8284f, 16.5f, 9f, 16.5f)
                    curveTo(8.1716f, 16.5f, 7.5f, 17.1716f, 7.5f, 18f)
                    moveTo(3.75f, 18f)
                    lineTo(7.5f, 18f)
                    moveTo(16.5f, 12f)
                    lineTo(20.25f, 12f)
                    moveTo(16.5f, 12f)
                    curveTo(16.5f, 12.8284f, 15.8284f, 13.5f, 15f, 13.5f)
                    curveTo(14.1716f, 13.5f, 13.5f, 12.8284f, 13.5f, 12f)
                    moveTo(16.5f, 12f)
                    curveTo(16.5f, 11.1716f, 15.8284f, 10.5f, 15f, 10.5f)
                    curveTo(14.1716f, 10.5f, 13.5f, 11.1716f, 13.5f, 12f)
                    moveTo(3.75f, 12f)
                    horizontalLineTo(13.5f)
                }
            }
            .build()
            .also {
                customize = it
            }
    }

private var customize: ImageVector? = null

val AppIcons.MagicWand: ImageVector
    get() {
        magicWand?.let { return it }

        return ImageVector
            .Builder(
                name = "Magic",
                defaultWidth = 16.dp,
                defaultHeight = 16.dp,
                viewportWidth = 16f,
                viewportHeight = 16f
            )
            .apply {
                path(
                    fill = SolidColor(Color(0xFF000000)),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(9.5f, 2.672f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 1f, 0f)
                    verticalLineTo(0.843f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, 0f)
                    close()
                    moveToRelative(4.5f, 0.035f)
                    arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.293f, 2f)
                    lineTo(12f, 3.293f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0.707f, 0.707f)
                    close()
                    moveTo(7.293f, 4f)
                    arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 8f, 3.293f)
                    lineTo(6.707f, 2f)
                    arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6f, 2.707f)
                    close()
                    moveToRelative(-0.621f, 2.5f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, -1f)
                    horizontalLineTo(4.843f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, 1f)
                    close()
                    moveToRelative(8.485f, 0f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, -1f)
                    horizontalLineToRelative(-1.829f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1f)
                    close()
                    moveTo(13.293f, 10f)
                    arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 14f, 9.293f)
                    lineTo(12.707f, 8f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, -0.707f, 0.707f)
                    close()
                    moveTo(9.5f, 11.157f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1f, 0f)
                    verticalLineTo(9.328f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, 0f)
                    close()
                    moveToRelative(1.854f, -5.097f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -0.706f)
                    lineToRelative(-0.708f, -0.708f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.707f, 0f)
                    lineTo(8.646f, 5.94f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 0.707f)
                    lineToRelative(0.708f, 0.708f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.707f, 0f)
                    lineToRelative(1.293f, -1.293f)
                    close()
                    moveToRelative(-3f, 3f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -0.706f)
                    lineToRelative(-0.708f, -0.708f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.707f, 0f)
                    lineTo(0.646f, 13.94f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 0.707f)
                    lineToRelative(0.708f, 0.708f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.707f, 0f)
                    close()
                }
            }
            .build()
            .also { magicWand = it }
    }

private var magicWand: ImageVector? = null
