package com.example.pdp.ui.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToInt

// Custom composable that draws a radial progress bar with a unique gradient fill
@Composable
fun CustomCircularProgressBar(
    size: Dp = 96.dp,
    strokeWidth: Dp = 12.dp,
    progress: Float = 0f,
    startAngle: Float = 270f,
    progressArcColor1: Color = Color.Blue,
    progressArcColor2: Color = progressArcColor1,
    progressArcColor3: Color = progressArcColor2,
    animationOn: Boolean = false
) {
    val currentProgress = remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress.floatValue,
        animationSpec = spring(stiffness = Spring.StiffnessLow), // Spring-based easing function
        label = "Progress Animation"
    )
    LaunchedEffect(animationOn, progress) {
        if (animationOn) {
            progressFlow(progress).collect { value ->
                currentProgress.floatValue = value
            }
        } else {
            currentProgress.floatValue = progress
        }
    }

    val textMeasurer = TextMeasurer(
        defaultDensity = LocalDensity.current,
        defaultFontFamilyResolver = LocalFontFamilyResolver.current,
        defaultLayoutDirection = LocalLayoutDirection.current
    )

    Canvas(modifier = Modifier.size(size)) {
        val strokeWidthPx = strokeWidth.toPx()
        val arcSize = size.toPx() - strokeWidthPx
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(progressArcColor1, progressArcColor2, progressArcColor3)
        )
        withTransform({
            rotate(degrees = startAngle, pivot = center)
        }) {
            drawArc(
                brush = gradientBrush,
                startAngle = -110f, // Start from the bottom left
                sweepAngle = animatedProgress * 220, // Sweep 220 degrees to bottom right
                useCenter = false,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }

        val textLayoutResult = textMeasurer.measure(
            text = "${(animatedProgress * 100).roundToInt()}%",
            style = TextStyle(fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        )
        val centerOffset = Offset(
            (center.x),
            (center.y )
        )
        drawText(
            textLayoutResult = textLayoutResult,
            color = Color.Black,
            topLeft = centerOffset - (textLayoutResult.size / 2).toOffset() + Offset(0f,
                (-textLayoutResult.size.height / 2).toFloat()
            )
        )
    }
}

fun IntSize.toOffset() = Offset(width.toFloat(), height.toFloat())

fun progressFlow(
    targetProgress: Float = 1f,
    step: Float = 0.01f,
    delayTime: Long = 1L
): Flow<Float> {
    return flow {
        var progress = 0f
        while (progress <= targetProgress) {
            emit(progress)
            progress += step
            delay(delayTime)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomerCircularProgressBarPreview() {
    CustomCircularProgressBar(
        progress = 50f
    )
}