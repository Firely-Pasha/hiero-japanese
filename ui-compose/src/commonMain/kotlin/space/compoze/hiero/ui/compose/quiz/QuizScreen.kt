@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package space.compoze.hiero.ui.compose.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CropPortrait
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemVariantModel
import space.compoze.hiero.domain.collectionitem.model.data.isPrimary
import space.compoze.hiero.domain.collectionitem.model.data.isSecondary
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation
import space.compoze.hiero.ui.compose.modal.Anchor
import space.compoze.hiero.ui.compose.modal.HieroModalConsumer
import space.compoze.hiero.ui.compose.modal.HieroModalConsumerScope
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.quiz.component.QuizComponent
import space.compoze.hiero.ui.shared.quiz.store.QuizStore
import kotlin.reflect.KFunction0

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
            onBookmark = component::bookmarkCurrentItem,
            onSwapVariants = component::swapVariants,
        )
    }
}

@Composable
fun QuizContent(
    state: QuizStore.State.Content,
    onNavigateBack: () -> Unit,
    onNextItemClick: () -> Unit,
    onBookmark: () -> Unit,
    onSwapVariants: () -> Unit,
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
                val primaryVariant by derivedStateOf {
                    val section = state.sections.find { it.id == targetState.sectionId }
                    val variants =
                        state.variants.filter { it.collectionId == section?.collectionId }
                    variants.find { if (state.areVariantsSwapped) it.isSecondary() else it.isPrimary() }!!
                }
                val secondaryVariant by derivedStateOf {
                    val section = state.sections.find { it.id == targetState.sectionId }
                    val variants =
                        state.variants.filter { it.collectionId == section?.collectionId }
                    variants.find { if (state.areVariantsSwapped) it.isPrimary() else it.isSecondary() }!!
                }
                val currentVariant by derivedStateOf {
                    if (isFlipped) secondaryVariant else primaryVariant
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(26.dp),
                        onClick = {
                            isFlipped = !isFlipped
                        }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            BookmarkIcon(
                                modifier = Modifier
                                    .align(Alignment.TopEnd),
                                isBookmarked = state.currentItem.isBookmarked,
                                onBookmark = onBookmark
                            )
                            CardSideIcon(
                                modifier = Modifier
                                    .align(Alignment.TopStart),
                                isFlipped = isFlipped,
                                onFlip = {
                                    isFlipped = !isFlipped
                                }
                            )
                            Text(
                                text = targetState.variants[currentVariant.id].orEmpty(),
                                fontSize = 112.sp,
                                modifier = Modifier
                                    .align(Alignment.Center),
                            )
                        }
                    }
                    VariantLabel(
                        primaryVariant = primaryVariant,
                        secondaryVariant = secondaryVariant,
                        currentVariant = currentVariant,
                        onSwapVariants = {
                            onSwapVariants()
                            isFlipped = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarkIcon(
    modifier: Modifier,
    isBookmarked: Boolean,
    onBookmark: () -> Unit,
) {

    Box(
        modifier = modifier
            .offset(x = (-8).dp, y = (8).dp)
    ) {
        IconButton(onBookmark) {
            AnimatedContent(
                isBookmarked,
            ) { state ->
                Icon(
                    if (state) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    "Not bookmarked"
                )
            }
        }
    }
}

@Composable
private fun CardSideIcon(
    modifier: Modifier,
    isFlipped: Boolean,
    onFlip: () -> Unit,
) {
    Box(
        modifier = modifier
            .offset(x = 8.dp, y = 8.dp)
    ) {
        IconButton(onFlip) {
            Box(
                modifier = Modifier
                    .width(14.dp)
                    .aspectRatio(3 / 4f)
                    .clip(shape = RoundedCornerShape(2.dp))
                    .run {
                        if (isFlipped) {
                            background(LocalContentColor.current)
                        } else {
                            this
                        }
                    }
                    .border(
                        width = 1.95.dp,
                        color = LocalContentColor.current
                    )
            )
        }
    }
}

@Composable
private fun VariantLabel(
    primaryVariant: CollectionItemVariantModel,
    secondaryVariant: CollectionItemVariantModel,
    currentVariant: CollectionItemVariantModel,
    onSwapVariants: () -> Unit,
) {
    HieroModalConsumer {
        Surface(
            modifier = Modifier
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
            onClick = {
                showDialogue(
                    alignment = Anchor.Center,
                    width = 300.dp
                ) {
                    ChangeVariantDialog(
                        primaryVariant = primaryVariant,
                        secondaryVariant = secondaryVariant,
                        onOk = onSwapVariants
                    )
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                text = currentVariant.name,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun HieroModalConsumerScope.ChangeVariantDialog(
    primaryVariant: CollectionItemVariantModel,
    secondaryVariant: CollectionItemVariantModel,
    onCancel: (() -> Unit)? = null,
    onOk: () -> Unit,
) {
    Card {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "Sides will be swapped",
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Primary",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                // Swapping it manually,
                // because they are not swapping... for some reasons
                // TODO: Investigate it later
                Text(secondaryVariant.name)
            }
            Row {
                Text(
                    "Secondary",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(primaryVariant.name)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton({ onCancel?.invoke(); dismissDialogue() }) {
                    Text("Cancel")
                }
                Button({ onOk(); dismissDialogue() }) {
                    Text("OK")
                }
            }
        }
    }
}