@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@file:Suppress("NAME_SHADOWING")

package space.compoze.hiero.ui.compose.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import space.compoze.hiero.domain.collectionitem.enums.CollectionItemType
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.ui.compose.modal.Anchor
import space.compoze.hiero.ui.compose.modal.HieroModalConsumer
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.section.component.SectionComponent
import space.compoze.hiero.ui.shared.section.state.SectionStore
import kotlin.random.Random

@Composable
fun SectionScreen(component: SectionComponent) {

    val state by component.state.subscribeAsState()

    when (val state = state) {
        SectionStore.State.Loading -> CircularProgressIndicator()
        is SectionStore.State.Error -> Text(
            state.error.message ?: state.error.cause?.message ?: "UNKNOWN ERROR WTF?????"
        )

        is SectionStore.State.Content -> SectionContent(
            state = state,
            onNavigateBack = component::navigateBack,
            onSelectAllClick = component::selectAll,
            onClearAllClick = component::clearAll,
            onStartQuizClick = component::startQuiz,
            onItemSelect = component::toggleItemSelect,
            onItemBookmark = component::toggleItemBookmark,
            onToggleItemAndSetSelectMode = component::toggleItemAndSetSelectMode,
            onToggleItemBySelect = component::toggleItemBySelect
        )
    }

}

@Composable
private fun SectionContent(
    state: SectionStore.State.Content,
    onNavigateBack: () -> Unit,
    onClearAllClick: () -> Unit,
    onSelectAllClick: () -> Unit,
    onStartQuizClick: () -> Unit,
    onItemSelect: (Long) -> Unit,
    onItemBookmark: (Long) -> Unit,
    onToggleItemAndSetSelectMode: (itemId: Long) -> Unit,
    onToggleItemBySelect: (itemId: Long) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SectionTopBar(
                state = state,
                scrollBehavior = scrollBehavior,
                onSelectAllClick = onSelectAllClick,
                onClearAllClick = onClearAllClick,
                onNavigateBack = onNavigateBack,
            )
        },
        floatingActionButton = {
            val isSelectedAny = remember(state.items) {
                state.items.any { it.isSelected || it.isBookmarked }
            }
            AnimatedVisibility(
                isSelectedAny,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = onStartQuizClick,
                ) {
                    Icon(Icons.Rounded.PlayArrow, "as")
                }
            }
        }
    ) {
        val containerPadding = remember(Unit) {
            val padding = 16.dp
            PaddingValues(
                start = it.calculateStartPadding(LayoutDirection.Ltr) + padding,
                top = it.calculateTopPadding() + padding,
                end = it.calculateEndPadding(LayoutDirection.Ltr) + padding,
                bottom = it.calculateBottomPadding() + padding + 64.dp,
            )
        }

        val lazyGridState = rememberLazyGridState()
        var offset by remember { mutableStateOf(Offset.Zero) }
        var currentItem by remember { mutableStateOf<Option<LazyGridItemInfo>>(None) }
        LazyVerticalGrid(
            state = lazyGridState,
            contentPadding = containerPadding,
            columns = GridCells.Fixed(state.collection.cols),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .composed {
                    pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { initialOffset ->
                                offset = initialOffset - Offset(
                                    containerPadding.calculateStartPadding(LayoutDirection.Ltr)
                                        .toPx(),
                                    lazyGridState.layoutInfo.beforeContentPadding.toFloat()
                                )
                                for (item in lazyGridState.layoutInfo.visibleItemsInfo) {
                                    val isInX =
                                        offset.x.toInt() in item.offset.x..(item.offset.x + item.size.width)
                                    val isInY =
                                        offset.y.toInt() in item.offset.y..(item.offset.y + item.size.height)
                                    if (isInX && isInY && !currentItem.isSome { it.index == item.index }) {
                                        currentItem = Some(item)
                                        val item = state.items[item.index]
                                        onToggleItemAndSetSelectMode(item.id)
                                    }
                                }

                            },
                            onDrag = { _, dragAmount ->
                                offset += dragAmount
                                for (item in lazyGridState.layoutInfo.visibleItemsInfo) {
                                    val isInX =
                                        offset.x.toInt() in item.offset.x..(item.offset.x + item.size.width)
                                    val isInY =
                                        offset.y.toInt() in item.offset.y..(item.offset.y + item.size.height)
                                    if (isInX && isInY && !currentItem.isSome { it.index == item.index }) {
                                        currentItem = Some(item)
                                        val item = state.items[item.index]
                                        onToggleItemBySelect(item.id)
                                    }
                                }
                            },
                            onDragEnd = {
                                currentItem = None
                            }
                        )
                    }
                }
        ) {
            items(
                items = state.items,
                key = { it.id },
                span = { GridItemSpan(state.section.span) },
                contentType = { it.type }
            ) { item ->
                SectionItem(
                    item = item,
                    onItemSelect = onItemSelect,
                    onItemBookmark = onItemBookmark
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SectionTopBar(
    state: SectionStore.State.Content,
    scrollBehavior: TopAppBarScrollBehavior,
    onSelectAllClick: () -> Unit,
    onClearAllClick: () -> Unit,
    onNavigateBack: () -> Unit,
) {

    var isMenuOpen by remember { mutableStateOf(false) }

    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton({
                onNavigateBack()
            }) {
                Icon(Icons.Rounded.ArrowBack, "Back")
            }
        },
        title = {
            Column {
                Text(
                    state.section.title,
                    maxLines = 1
                )
                Text(
                    state.collection.title,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        actions = {
            HieroModalConsumer {
                IconButton(
                    onClick = {
                        show(
                            anchor = Anchor.TopEnd,
                            alignment = Anchor.TopEnd,
                            width = 128.dp,
                            offset = Offset(-12f, 0f)
                        ) {
                            Card(
                                Modifier.background(Color.Transparent)
                            ) {
                                Column {
                                    ListItem(
                                        modifier = Modifier
                                            .clickable(onClick = {
                                                onSelectAllClick()
                                                dismiss()
                                            }),
                                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                                        headlineText = {
                                            Text("Select all")
                                        }
                                    )
                                    ListItem(
                                        modifier = Modifier
                                            .clickable(onClick = {
                                                onClearAllClick()
                                                dismiss()
                                            }),
                                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                                        headlineText = {
                                            Text("Clear all")
                                        },
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Icon(Icons.Rounded.MoreVert, "Actions")
                }
            }
        }
    )
}

@Composable
private fun SectionItem(
    item: CollectionItemModel,
    onItemSelect: (Long) -> Unit,
    onItemBookmark: (Long) -> Unit,
) {
    Box {
        if (item.type == CollectionItemType.HIEROGLYPH) {
            Card(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .combinedClickable(
                        onLongClick = { onItemBookmark(item.id) },
                        onClick = { onItemSelect(item.id) }
                    )
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
//                    containerColor = randomColor()
                )
            ) {
                Box(
                    modifier = Modifier.padding(6.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            item.value,
                            fontSize = 32.sp,
                            softWrap = false
                        )
                        Text(
                            item.transcription,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            AnimatedVisibility(
                item.isSelected,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .offset(y = (-8).dp, x = (-8).dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                ) {
                    Box(
                        Modifier
                            .padding(2.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .fillMaxSize()
                    ) {
                        Icon(
                            Icons.Outlined.School,
                            "Selected",
                            modifier = Modifier
                                .height(16.dp)
                                .width(16.dp)
                                .align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            AnimatedVisibility(
                item.isBookmarked,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .offset(y = (-8).dp, x = (-8).dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                ) {
                    Box(
                        Modifier
                            .padding(2.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .fillMaxSize()
                    ) {
                        Icon(
                            Icons.Outlined.BookmarkBorder,
                            "Selected",
                            modifier = Modifier
                                .height(16.dp)
                                .width(16.dp)
                                .align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

fun randomColor() = Color(
    Random.nextInt(256),
    Random.nextInt(256),
    Random.nextInt(256)
)
