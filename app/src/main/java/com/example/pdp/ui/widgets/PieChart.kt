package com.example.pdp.ui.widgets

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.random.Random

data class PieChartSection(
    val value: Float,
    val color: Color
)

@Composable
fun PieChartInstance(onUserInteraction: () -> Unit) {
    val initialSections = listOf(
        PieChartSection(30f, Color.Red),
        PieChartSection(70f, Color.Blue),
        PieChartSection(50f, Color.Green),
        PieChartSection(40f, Color.Yellow)
    )
    var sections by remember { mutableStateOf(initialSections) }
    var recompositionCount by remember { mutableIntStateOf(0) }


    LaunchedEffect(sections) {
        recompositionCount++
    }

    Column(
        modifier = Modifier
            .clickable(enabled = false) {
                onUserInteraction()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = 20.dp),
            text = "Recomposed $recompositionCount times"
        )

        PieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .interactions {
                    onUserInteraction()
                },
            sections = sections,
            animationDuration = 1000,
            onSectionClick = { _ -> onUserInteraction() }
        )

        Button(
            modifier = Modifier
                .padding(top = 20.dp),
            onClick = {
                sections = sections.map { it.copy(value = Random.nextFloat() * 100) }
                onUserInteraction()
            }
        ) {
            Text(text = "Recompose ^_^")
        }
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    sections: List<PieChartSection>,
    animated: Boolean = true,
    animationDuration: Int = 1000,
    onSectionClick: ((index: Int) -> Unit)? = null
) {
    val totalValue = sections.sumOf { it.value.toDouble() }.toFloat()

    val animatedProgress = if (animated) {
        animateFloatAsState(
            targetValue = totalValue,
            animationSpec = TweenSpec(durationMillis = animationDuration),
            label = ""
        ).value
    } else totalValue

    val anglePerValue = 360f / totalValue
    var startAngle = 0f
    var clickedSection by remember { mutableStateOf<Int?>(null) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val tapX = tapOffset.x - size.width / 2
                    val tapY = tapOffset.y - size.height / 2
                    val clickAngle = (atan2(tapY, tapX) * 180f / PI.toFloat() + 360f) % 360f
                    var currentAngle = 0f
                    sections.forEachIndexed { index, section ->
                        val sectionAngle = section.value * anglePerValue
                        if (clickAngle in currentAngle..(currentAngle + sectionAngle)) {
                            clickedSection = index
                            onSectionClick?.invoke(index)
                            return@detectTapGestures
                        }
                        currentAngle += sectionAngle
                    }
                }
            }
    ) {
        val chartSize = size.minDimension

        sections.forEachIndexed { index, section ->
            val sweepAngle = (section.value / totalValue) * animatedProgress * anglePerValue

            val animatedSweepAngle = (section.value / totalValue) * animatedProgress * anglePerValue

            drawArc(
                color = section.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = Size(chartSize, chartSize),
                topLeft = Offset(
                    (size.width - chartSize) / 2,
                    (size.height - chartSize) / 2
                )
            )

            val radiusOffset = 0.dp.toPx()

            // Draw border around the clicked section
            if (index == clickedSection) {
                drawArc(
                    color = Color.Black,
                    startAngle = startAngle,
                    sweepAngle = animatedSweepAngle,
                    useCenter = false,
                    size = Size(chartSize + radiusOffset, chartSize + radiusOffset),
                    topLeft = Offset(
                        (size.width - chartSize - radiusOffset) / 2,
                        (size.height - chartSize - radiusOffset) / 2
                    ),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            }


            startAngle += sweepAngle
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DynamicPieChartPreview() {
    val initialSections = listOf(
        PieChartSection(30f, Color.Red),
        PieChartSection(70f, Color.Blue),
        PieChartSection(50f, Color.Green),
        PieChartSection(40f, Color.Yellow)
    )

    PieChart(
        sections = initialSections,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        animationDuration = 1000,
        onSectionClick = { _ -> }
    )
}
