package com.example.pdp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pdp.ui.widgets.CustomCircularProgressBar
import com.example.pdp.ui.widgets.CustomScaffold1
import com.example.pdp.ui.widgets.DebouncingTextField
import com.example.pdp.ui.widgets.PieChartInstance
import com.example.pdp.ui.widgets.keyboardAsState
import com.example.pdp.ui.widgets.progressFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val keyboardState = keyboardAsState()
            val scope = rememberCoroutineScope()
            val lastInteractionTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
            val inactivityTimeout = 5000L

            LaunchedEffect(key1 = Unit) {
                scope.launch {
                    while (true) {
                        delay(2000)
                        Log.d("MainActivity", "keyboardState: $keyboardState")
                    }
                }
            }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastInteractionTime.longValue >= inactivityTimeout) {
                        Log.d("MainActivity", "on IDLE")
                    }
                    delay(inactivityTimeout)
                }
            }

            fun onUserInteraction() {
                lastInteractionTime.longValue = System.currentTimeMillis()
            }

            val progressFlow = remember { progressFlow(delayTime = 100L) }
            val progressState = progressFlow.collectAsState(initial = 0f)
            val list = mutableListOf(
                "apple",
                "cherry",
                "banana",
                "avocado",
                "orange",
                "strawberry",
                "cucumber",
                "watermelon",
                "blueberry",
                "apple",
                "pineapple",
                "melon",
                "pear"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CustomCircularProgressBar(
                            size = 200.dp,
                            strokeWidth = 8.dp,
                            progress = progressState.value,
                            progressArcColor1 = Color(0xFF673AB7),
                            progressArcColor2 = Color(0xFF4CAF50),
                            progressArcColor3 = Color(0xFF3F51B5)
                        )
                    }
                    PieChartInstance(onUserInteraction = ::onUserInteraction)
                    DebouncingTextField(
                        modifier = Modifier,
                        onUserInteraction = ::onUserInteraction,
                        initialText = ""
                    ) {
                        Log.d("MainActivity", "Debouncing: $it")
                    }
                    ScaffoldScreen(list = list)
                }
            }
        }
    }
}

@Composable
fun ScaffoldScreen(list: List<String>) {
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = list.size - 1)
    val scope = rememberCoroutineScope()

    CustomScaffold1(
        floatingButtons = {
            FloatingActionButton(
                onClick = {
                    scope.launch { lazyListState.animateScrollToItem(0, 0) }
                },
                containerColor = Color.Red
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(list) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp))
                }
            }
        }
    )
}

