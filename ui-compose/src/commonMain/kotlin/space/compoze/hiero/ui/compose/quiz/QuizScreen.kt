@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package space.compoze.hiero.ui.compose.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.quiz.component.QuizComponent
import space.compoze.hiero.ui.shared.quiz.store.QuizStore

@Composable
fun QuizScreen(component: QuizComponent) {

    val state by component.state.subscribeAsState()

    when (val state = state) {
        QuizStore.State.Loading -> Text("LOADUN")
        QuizStore.State.Error -> Text("EERRRIR")
        is QuizStore.State.Content -> QuizContent(
            state = state,
            onNavigateBack = component::navigateBack,
            onNextItemClick = component::nextItem,
            onBookmark = component::bookmarkCurrentItem
        )
    }
}

@Composable
fun QuizContent(
    state: QuizStore.State.Content,
    onNavigateBack: () -> Unit,
    onNextItemClick: () -> Unit,
    onBookmark: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onNavigateBack) {
                        Icon(Icons.Rounded.ArrowBack, "Back")
                    }
                },
                title = {},
                colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = onNextItemClick
            ) {
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
                val primaryVariant = remember {
                    val section = state.sections.find { it.id == targetState.sectionId }
                    val variants = state.variants.filter { it.collectionId == section?.collectionId }
                    variants.find { it.type == 1L }!!
                }
                val secondaryVariant = remember {
                    val section = state.sections.find { it.id == targetState.sectionId }
                    val variants = state.variants.filter { it.collectionId == section?.collectionId }
                    variants.find { it.type == 2L }!!
                }
                val currentVariant by derivedStateOf {
                    if (isFlipped) secondaryVariant else primaryVariant
                }
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
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-8).dp, y = (8).dp)
                        ) {
                            IconButton(onBookmark) {
                                AnimatedContent(
                                    state.currentItem.isBookmarked,
                                ) { state ->
                                    Icon(
                                        if (state) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                                        "Not bookmarked"
                                    )
                                }
                            }
                        }
                        Text(
                            text = targetState.variants[currentVariant.id].orEmpty(),
                            fontSize = 112.sp,
                            modifier = Modifier
                                .align(Alignment.Center),
                        )
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(8.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            onClick = {}
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                ,
                                text = currentVariant.name,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}