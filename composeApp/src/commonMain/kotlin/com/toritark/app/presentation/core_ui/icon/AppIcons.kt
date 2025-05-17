package com.toritark.app.presentation.core_ui.icon

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

val AppIcons.Story: ImageVector
    get() {
        story?.let { return it }

        return ImageVector
            .Builder(
                name = "Menu_book",
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
                    moveTo(560f, 396f)
                    verticalLineToRelative(-68f)
                    quadToRelative(33f, -14f, 67.5f, -21f)
                    reflectiveQuadToRelative(72.5f, -7f)
                    quadToRelative(26f, 0f, 51f, 4f)
                    reflectiveQuadToRelative(49f, 10f)
                    verticalLineToRelative(64f)
                    quadToRelative(-24f, -9f, -48.5f, -13.5f)
                    reflectiveQuadTo(700f, 360f)
                    quadToRelative(-38f, 0f, -73f, 9.5f)
                    reflectiveQuadTo(560f, 396f)
                    moveToRelative(0f, 220f)
                    verticalLineToRelative(-68f)
                    quadToRelative(33f, -14f, 67.5f, -21f)
                    reflectiveQuadToRelative(72.5f, -7f)
                    quadToRelative(26f, 0f, 51f, 4f)
                    reflectiveQuadToRelative(49f, 10f)
                    verticalLineToRelative(64f)
                    quadToRelative(-24f, -9f, -48.5f, -13.5f)
                    reflectiveQuadTo(700f, 580f)
                    quadToRelative(-38f, 0f, -73f, 9f)
                    reflectiveQuadToRelative(-67f, 27f)
                    moveToRelative(0f, -110f)
                    verticalLineToRelative(-68f)
                    quadToRelative(33f, -14f, 67.5f, -21f)
                    reflectiveQuadToRelative(72.5f, -7f)
                    quadToRelative(26f, 0f, 51f, 4f)
                    reflectiveQuadToRelative(49f, 10f)
                    verticalLineToRelative(64f)
                    quadToRelative(-24f, -9f, -48.5f, -13.5f)
                    reflectiveQuadTo(700f, 470f)
                    quadToRelative(-38f, 0f, -73f, 9.5f)
                    reflectiveQuadTo(560f, 506f)
                    moveTo(260f, 640f)
                    quadToRelative(47f, 0f, 91.5f, 10.5f)
                    reflectiveQuadTo(440f, 682f)
                    verticalLineToRelative(-394f)
                    quadToRelative(-41f, -24f, -87f, -36f)
                    reflectiveQuadToRelative(-93f, -12f)
                    quadToRelative(-36f, 0f, -71.5f, 7f)
                    reflectiveQuadTo(120f, 268f)
                    verticalLineToRelative(396f)
                    quadToRelative(35f, -12f, 69.5f, -18f)
                    reflectiveQuadToRelative(70.5f, -6f)
                    moveToRelative(260f, 42f)
                    quadToRelative(44f, -21f, 88.5f, -31.5f)
                    reflectiveQuadTo(700f, 640f)
                    quadToRelative(36f, 0f, 70.5f, 6f)
                    reflectiveQuadToRelative(69.5f, 18f)
                    verticalLineToRelative(-396f)
                    quadToRelative(-33f, -14f, -68.5f, -21f)
                    reflectiveQuadToRelative(-71.5f, -7f)
                    quadToRelative(-47f, 0f, -93f, 12f)
                    reflectiveQuadToRelative(-87f, 36f)
                    close()
                    moveToRelative(-40f, 118f)
                    quadToRelative(-48f, -38f, -104f, -59f)
                    reflectiveQuadToRelative(-116f, -21f)
                    quadToRelative(-42f, 0f, -82.5f, 11f)
                    reflectiveQuadTo(100f, 762f)
                    quadToRelative(-21f, 11f, -40.5f, -1f)
                    reflectiveQuadTo(40f, 726f)
                    verticalLineToRelative(-482f)
                    quadToRelative(0f, -11f, 5.5f, -21f)
                    reflectiveQuadTo(62f, 208f)
                    quadToRelative(46f, -24f, 96f, -36f)
                    reflectiveQuadToRelative(102f, -12f)
                    quadToRelative(58f, 0f, 113.5f, 15f)
                    reflectiveQuadTo(480f, 220f)
                    quadToRelative(51f, -30f, 106.5f, -45f)
                    reflectiveQuadTo(700f, 160f)
                    quadToRelative(52f, 0f, 102f, 12f)
                    reflectiveQuadToRelative(96f, 36f)
                    quadToRelative(11f, 5f, 16.5f, 15f)
                    reflectiveQuadToRelative(5.5f, 21f)
                    verticalLineToRelative(482f)
                    quadToRelative(0f, 23f, -19.5f, 35f)
                    reflectiveQuadToRelative(-40.5f, 1f)
                    quadToRelative(-37f, -20f, -77.5f, -31f)
                    reflectiveQuadTo(700f, 720f)
                    quadToRelative(-60f, 0f, -116f, 21f)
                    reflectiveQuadToRelative(-104f, 59f)
                    moveTo(280f, 466f)
                }
            }
            .build()
            .also { story = it }
    }

private var story: ImageVector? = null


val AppIcons.Quiz: ImageVector
    get() {
        quiz?.let { return it }

        return ImageVector
            .Builder(
                name = "Indeterminate_question_box",
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
                    moveTo(200f, 840f)
                    quadToRelative(-33f, 0f, -56.5f, -23.5f)
                    reflectiveQuadTo(120f, 760f)
                    verticalLineToRelative(-160f)
                    horizontalLineToRelative(80f)
                    verticalLineToRelative(160f)
                    horizontalLineToRelative(160f)
                    verticalLineToRelative(80f)
                    close()
                    moveToRelative(560f, 0f)
                    horizontalLineTo(600f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(160f)
                    verticalLineToRelative(-160f)
                    horizontalLineToRelative(80f)
                    verticalLineToRelative(160f)
                    quadToRelative(0f, 33f, -23.5f, 56.5f)
                    reflectiveQuadTo(760f, 840f)
                    moveTo(120f, 200f)
                    quadToRelative(0f, -33f, 23.5f, -56.5f)
                    reflectiveQuadTo(200f, 120f)
                    horizontalLineToRelative(160f)
                    verticalLineToRelative(80f)
                    horizontalLineTo(200f)
                    verticalLineToRelative(160f)
                    horizontalLineToRelative(-80f)
                    close()
                    moveToRelative(720f, 0f)
                    verticalLineToRelative(160f)
                    horizontalLineToRelative(-80f)
                    verticalLineToRelative(-160f)
                    horizontalLineTo(600f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(160f)
                    quadToRelative(33f, 0f, 56.5f, 23.5f)
                    reflectiveQuadTo(840f, 200f)
                    moveTo(480f, 720f)
                    quadToRelative(21f, 0f, 35.5f, -14.5f)
                    reflectiveQuadTo(530f, 670f)
                    reflectiveQuadToRelative(-14.5f, -35.5f)
                    reflectiveQuadTo(480f, 620f)
                    reflectiveQuadToRelative(-35.5f, 14.5f)
                    reflectiveQuadTo(430f, 670f)
                    reflectiveQuadToRelative(14.5f, 35.5f)
                    reflectiveQuadTo(480f, 720f)
                    moveToRelative(-36f, -153f)
                    horizontalLineToRelative(73f)
                    quadToRelative(0f, -34f, 8f, -52f)
                    reflectiveQuadToRelative(35f, -45f)
                    quadToRelative(35f, -35f, 46.5f, -56.5f)
                    reflectiveQuadTo(618f, 362f)
                    quadToRelative(0f, -54f, -39f, -88f)
                    reflectiveQuadToRelative(-99f, -34f)
                    quadToRelative(-50f, 0f, -86f, 26f)
                    reflectiveQuadToRelative(-52f, 74f)
                    lineToRelative(66f, 27f)
                    quadToRelative(7f, -26f, 26.5f, -42.5f)
                    reflectiveQuadTo(480f, 308f)
                    quadToRelative(29f, 0f, 46.5f, 15.5f)
                    reflectiveQuadTo(544f, 365f)
                    quadToRelative(0f, 20f, -9.5f, 37.5f)
                    reflectiveQuadTo(502f, 439f)
                    quadToRelative(-33f, 29f, -45.5f, 56f)
                    reflectiveQuadTo(444f, 567f)
                }
            }
            .build()
            .also { quiz = it }
    }

private var quiz: ImageVector? = null