@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("NAME_SHADOWING")

package space.compoze.hiero.ui.compose.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import space.compoze.hiero.domain.collectionitem.enums.CollectionItemType
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel
import space.compoze.hiero.domain.section.model.SectionModel
import space.compoze.hiero.ui.compose.utils.select
import space.compoze.hiero.ui.compose.utils.subscribeAsState
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
            onItemClick = remember(component) {
                {
                    component.selectItem(it.id)
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
    onItemClick: (CollectionItemModel) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SectionTopBar(
                state,
                scrollBehavior,
                onNavigateBack,
            )
        },
        floatingActionButton = {
            FloatingActionButton({}) {
                Icon(Icons.Rounded.PlayArrow, "as")
            }
        }
    ) {
        val containerPadding = remember(Unit) {
            val padding = 16.dp
            PaddingValues(
                start = it.calculateStartPadding(LayoutDirection.Ltr) + padding,
                top = it.calculateTopPadding() + padding,
                end = it.calculateEndPadding(LayoutDirection.Ltr) + padding,
                bottom = it.calculateBottomPadding() + padding,
            )
        }


//        val sectionItems = remember(content.items) {
//            println("LELEELLELE")
//            content.items.groupBy { it.sectionId }
//        }
        val lazyGridState = rememberLazyGridState()
        val sectionItems = remember(state.sections, state.items) {
            mutableStateMapOf<SectionModel, List<CollectionItemModel>>().also {
                it.putAll(
                    state.sections.associateWith { section ->
                        state.items.filter {
                            it.sectionId == section.id
                        }
                    }
                )
            }
        }
        val showTitle = remember(sectionItems) { sectionItems.size > 1 }
        println("RECOMPOSE SECTION IOTEMS")
        LazyVerticalGrid(
            state = lazyGridState,
            contentPadding = containerPadding,
            columns = GridCells.Fixed(15),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            sectionItems.forEach { section ->
                if (showTitle) {
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        val sectionHeaderStyle = remember {
                            TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            section.key.title,
                            style = sectionHeaderStyle
                        )
                    }
                }
                items(
                    items = section.value,
                    key = { it.id },
                    span = { GridItemSpan(if (section.key.id == "basic" || section.key.id == "voiced") 3 else 5) }
                ) { item ->
                    Box(
                        modifier = Modifier
                    ) {
                        if (item.type == CollectionItemType.HIEROGLYPH) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 6.dp),
//                                elevation = CardDefaults.cardElevation(6.dp),
                                onClick = {
                                    onItemClick(item)
                                },
                                colors = CardDefaults.cardColors(
                                    containerColor = randomColor()
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
    onNavigateBack: () -> Unit,
) {

    val isMenuOpenValue = remember { MutableValue(false) }
    val isMenuOpen by isMenuOpenValue.subscribeAsState()

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
                    if (!isMenuOpenValue.value) {
                        isMenuOpenValue.value = true
                    }
                }
            ) {
                Icon(Icons.Rounded.MoreVert, "Actions")
                Popup(
                    Alignment.TopStart,
                    offset = IntOffset(x = 0, y = 120),
                    onDismissRequest = { isMenuOpenValue.value = false }
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
                                        .clickable {

                                        },
                                    headlineText = {
                                        Text("Select all")
                                    }
                                )
                                ListItem(
                                    headlineText = {
                                        Text("Clear all")
                                    },
                                    modifier = Modifier
                                        .clickable {

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