@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package space.compoze.hiero.ui.compose.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.quiz.component.QuizComponent
import space.compoze.hiero.ui.shared.quiz.store.QuizState

@Composable
fun QuizScreen(component: QuizComponent) {

    val state by component.state.subscribeAsState()

    when (val state = state) {
        QuizState.Loading -> Text("LOADUN")
        QuizState.Error -> Text("EERRRIR")
        is QuizState.Content -> QuizContent(
            state = state,
            onNextItemClick = remember {
                {
                    component.nextItem()
                }
            }
        )
    }
}

@Composable
fun QuizContent(
    state: QuizState.Content,
    onNextItemClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton({
                onNextItemClick()
            }) {
                Icon(Icons.Rounded.NavigateNext, "Next")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            AnimatedContent(
                modifier = Modifier
                    .align(Alignment.Center),
                targetState = state.currentItem,
                contentAlignment = Alignment.Center,
            ) { targetState ->
                var isFlipped by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(26.dp),
                    onClick = {
                        isFlipped = !isFlipped
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = if (isFlipped) targetState.transcription else targetState.value,
                            fontSize = 112.sp,
                            modifier = Modifier
                                .align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}