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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.School
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
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.arkivanov.decompose.value.Value
import space.compoze.hiero.domain.collectionitem.enums.CollectionItemType
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.ui.compose.utils.select
import space.compoze.hiero.ui.shared.section.SectionComponent
import space.compoze.hiero.ui.shared.section.SectionState
import kotlin.random.Random

@Composable
fun SectionScreen(component: SectionComponent) {

    val state by component.state.select { it }

    when (val state = state) {
        SectionState.Loading -> CircularProgressIndicator()
        is SectionState.Error -> Text(
            state.error.message ?: state.error.cause?.message ?: "UNKNOWN ERROR WTF?????"
        )

        is SectionState.Content -> SectionContent(
            state = state,
            onNavigateBack = {
                component.navigateBack()
            },
            onSelectAllClick = remember {
                {
                    component.selectAll()
                }
            },
            onClearAllClick = remember {
                {
                    component.clearAll()
                }
            },
            onStartQuizClick = remember {
                {
                    component.startQuiz()
                }
            },
            onItemSelect = remember(component) {
                {
                    component.toggleItemSelect(it.id)
                }
            },
            onItemBookmark = remember(component) {
                {
                    component.toggleItemBookmark(it.id)
                }
            }
        )
    }

}

@Composable
fun <T : Any, R> Value<T>.selectContent(
    policy: SnapshotMutationPolicy<R> = structuralEqualityPolicy(),
    block: (SectionState.Content) -> R,
) =
    select(policy) { block(it as SectionState.Content) }

@Composable
private fun SectionContent(
    state: SectionState.Content,
    onNavigateBack: () -> Unit,
    onClearAllClick: () -> Unit,
    onSelectAllClick: () -> Unit,
    onStartQuizClick: () -> Unit,
    onItemSelect: (CollectionItemModel) -> Unit,
    onItemBookmark: (CollectionItemModel) -> Unit,
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
                state.items.any { it.isSelected }
            }
            AnimatedVisibility(
                isSelectedAny,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = onStartQuizClick
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


//        val sectionItems = remember(content.items) {
//            println("LELEELLELE")
//            content.items.groupBy { it.sectionId }
//        }
        val lazyGridState = rememberLazyGridState()
        val sectionItems = remember(state.sections, state.items) {
            buildList {
                state.sections.forEach { section ->
                    add(section to state.items.filter {
                        it.sectionId == section.id
                    })
                }
            }.toMutableStateList()
        }
        val showTitle = remember(sectionItems) { sectionItems.size > 1 }
        println("RECOMPOSE SECTION IOTEMS")
        LazyVerticalGrid(
            state = lazyGridState,
            contentPadding = containerPadding,
            columns = GridCells.Fixed(state.collection.cols),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            sectionItems.forEach { (section, items) ->
                if (showTitle) {
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        val sectionHeaderStyle = remember {
                            TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            section.title,
                            style = sectionHeaderStyle
                        )
                    }
                }
                items(
                    items = items,
                    key = { it.id },
                    span = { GridItemSpan(section.span) }
                ) { item ->
                    Box(
                        modifier = Modifier
                    ) {
                        if (item.type == CollectionItemType.HIEROGLYPH) {
                            Card(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .combinedClickable(
                                        onLongClick = {
                                            onItemBookmark(item)
                                        },
                                        onClick = {
                                            onItemSelect(item)
                                        }
                                    )
                                    .fillMaxSize(),
//                                    .padding(vertical = 6.dp),
//                                elevation = CardDefaults.cardElevation(6.dp),
//                                onClick = {
//                                },

                                colors = CardDefaults.cardColors(
//                                    containerColor = randomColor()
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
                                            Icons.Rounded.School,
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
                                    .offset(y = (-8).dp, x = (8).dp)
                                    .align(Alignment.TopEnd)
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
                                            Icons.Rounded.Bookmark,
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
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SectionTopBar(
    state: SectionState.Content,
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
            Text(
                "${state.collection.title}: ${
                    if (state.sections.size == 1) {
                        state.sections[0].title
                    } else {
                        "All"
                    }
                }"
            )
        },
        actions = {
            IconButton(
                onClick = {
                    if (!isMenuOpen) {
                        isMenuOpen = true
                    }
                }
            ) {
                Icon(Icons.Rounded.MoreVert, "Actions")
                Popup(
                    Alignment.TopStart,
                    offset = IntOffset(x = 0, y = 120),
                    onDismissRequest = { isMenuOpen = false }
                ) {
                    AnimatedVisibility(
                        isMenuOpen,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut(),
                    ) {
                        Card(
                            Modifier.background(Color.Transparent)
                                .width(200.dp),
                        ) {
                            Column {
                                ListItem(
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            onSelectAllClick()
                                            isMenuOpen = false
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
                                            isMenuOpen = false
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
            }
        }
    )
}

fun randomColor() = Color(
    Random.nextInt(256),
    Random.nextInt(256),
    Random.nextInt(256)
)