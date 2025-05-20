package com.toritark.app.presentation.story.detail.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val ARROW_SPREAD_ANGLE = (25 / 180.0 * PI).toFloat()

@Composable
internal fun GenerateStoryArrowHint(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Dp = 3.dp,
) {
    Canvas(modifier = modifier) {
        val strokeWidthPx = strokeWidth.toPx()
        val canvasWidth = size.width
        val canvasHeight = size.height

        val startX = canvasWidth * 0.5f
        val startY = canvasHeight * 0.99f

        val endX = canvasWidth * 0.9f
        val endY = canvasHeight * 0.02f

        val control1X = canvasWidth * 0.05f
        val control1Y = canvasHeight * 0.4f

        val control2X = canvasWidth
        val control2Y = canvasHeight * 0.8f

        val arrowShaftPath = Path().apply {
            moveTo(startX, startY)
            cubicTo(control1X, control1Y, control2X, control2Y, endX, endY)
        }

        val dashPathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(
                strokeWidthPx * 2f,
                strokeWidthPx * 2f,
            ),
            phase = 0f,
        )

        drawPath(
            path = arrowShaftPath,
            color = color,
            style = Stroke(
                width = strokeWidthPx,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = dashPathEffect,
            ),
        )

        val arrowHeadLength = (size.minDimension * 0.06f).coerceAtLeast(strokeWidthPx * 3.0f)

        val angleRad = atan2(endY - control2Y, endX - control2X)

        val arrowheadPath = Path().apply {
            moveTo(
                endX - arrowHeadLength * cos(angleRad - ARROW_SPREAD_ANGLE),
                endY - arrowHeadLength * sin(angleRad - ARROW_SPREAD_ANGLE)
            )
            lineTo(endX, endY)
            lineTo(
                endX - arrowHeadLength * cos(angleRad + ARROW_SPREAD_ANGLE),
                endY - arrowHeadLength * sin(angleRad + ARROW_SPREAD_ANGLE),
            )
        }

        drawPath(
            path = arrowheadPath,
            color = color,
            style = Stroke(
                width = strokeWidthPx,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
    }
}

@Preview
@Composable
private fun GenerateStoryArrowHintPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 600.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            GenerateStoryArrowHint(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                color = MaterialTheme.colorScheme.tertiary,
                strokeWidth = 1.5.dp,
            )
        }
    }
}