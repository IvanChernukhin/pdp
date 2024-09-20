package com.example.pdp.ui.widgets

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class DebouncingState(
    val text: String,
    var isEnabled: Boolean
)

@Composable
fun debouncingText(
    text: String,
    timeMillis: Long = 500,
    onDebounceText: (String) -> Unit
): Pair<String, (String) -> Unit> {
    var state: DebouncingState by remember { mutableStateOf(DebouncingState(text, true)) }
    val scope = rememberCoroutineScope()
    var job: Job

    fun onChange(newText: String) {
        if (state.isEnabled)
            state = state.copy(text = newText)
        else
            onDebounceText(state.text)
    }
    DisposableEffect(state) {
        job = scope.launch {
            state.isEnabled = false
            delay(timeMillis)
            state.isEnabled = true
            onChange(state.text)
        }
        onDispose {
            job.cancel()
        }
    }

    return Pair(state.text) { newText ->
        onChange(newText)
    }
}

@Composable
fun DebouncingTextField(
    modifier: Modifier,
    onUserInteraction: () -> Unit,
    initialText: String,
    onDebounce: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val (debouncedText, updateDebouncedText) = debouncingText(
        text = initialText,
        timeMillis = 3000
    ) { newText ->
        Log.d("DebouncingTextField", "onDebounce: $newText")
        onDebounce(newText)
    }

    Card(
        modifier = Modifier
            .padding(10.dp),
        shape = RoundedCornerShape(10),
        border = BorderStroke(width = 3.dp, color = Color.Black)
    ) {
        BasicTextField(
            modifier = modifier
                .padding(30.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        onUserInteraction()
                    }
                },
            textStyle = TextStyle.Default.copy(textAlign = TextAlign.Center),
            value = debouncedText,
            onValueChange = { updateDebouncedText(it); onUserInteraction() },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { updateDebouncedText("") }),
        )
    }
}

@Preview
@Composable
fun PreviewDebouncingTextField() {
    DebouncingTextField(
        modifier = Modifier,
        onUserInteraction = {},
        initialText = "Cringe",
        onDebounce = {}
    )
}
