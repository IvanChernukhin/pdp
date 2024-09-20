package com.example.pdp.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Probably the way it was meant to be
 */
@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    floatingButtons: @Composable () -> Unit,
    content: @Composable (IntSize) -> Unit
) {
    Layout(
        modifier = modifier,
        content = {
            floatingButtons()
            content(IntSize(0, 0))
        }
    ) { measurables, constraints ->
        val floatingButtonsPlaceable: Placeable = measurables[0].measure(constraints)
        val floatingButtonsHeight: Int = floatingButtonsPlaceable.height
        val floatingButtonsWidth: Int = floatingButtonsPlaceable.width

        val contentPlaceable: Placeable = measurables[1].measure(
            constraints.copy(maxHeight = constraints.maxHeight - floatingButtonsHeight)
        )

        val layoutWidth = maxOf(floatingButtonsWidth, contentPlaceable.width)
        val layoutHeight = contentPlaceable.height + floatingButtonsHeight

        layout(layoutWidth, layoutHeight) {
            contentPlaceable.place(0, 0)

            floatingButtonsPlaceable.place(
                x = (layoutWidth - floatingButtonsWidth) / 2,
                y = contentPlaceable.height
            )
        }
    }
}

/**
 * The way it works
 */
@Composable
fun CustomScaffold1(
    modifier: Modifier = Modifier,
    floatingButtons: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            floatingButtons()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomScaffold1() {
    CustomScaffold1(floatingButtons = { /*TODO*/ }) {
        FloatingActionButton(
            onClick = {},
            containerColor = Color.Red
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Scroll to top"
            )
        }
    }
}