@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("NAME_SHADOWING")

package space.compoze.hiero.ui.compose.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandHorizontally
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import space.compoze.hiero.domain.collectionitem.enums.CollectionItemType
import space.compoze.hiero.domain.section.model.SectionModel
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.section.SectionComponent
import space.compoze.hiero.ui.shared.section.SectionState

@Composable
fun SectionScreen(component: SectionComponent) {

    val state by component.state.subscribeAsState()

    when (val state = state) {
        SectionState.Loading -> CircularProgressIndicator()
        is SectionState.Error -> Text(
            state.error.message ?: state.error.cause?.message ?: "UNKNOWN ERROR WTF?????"
        )

        is SectionState.Content -> SectionContent(component, state)
    }

}

@Composable
private fun SectionContent(component: SectionComponent, content: SectionState.Content) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SectionTopBar(
                component,
                content,
                scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton({}) {
                Icon(Icons.Rounded.PlayArrow, "as")
            }
        }
    ) {
        val containerPadding = remember {
            val padding = 16.dp
            PaddingValues(
                start = it.calculateStartPadding(LayoutDirection.Ltr) + padding,
                top = it.calculateTopPadding() + padding,
                end = it.calculateEndPadding(LayoutDirection.Ltr) + padding,
                bottom = it.calculateBottomPadding() + padding,
            )
        }

        LazyVerticalGrid(
            contentPadding = containerPadding,
            columns = GridCells.Fixed(15),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for (section in content.sections) {
                section(component, content, section, showTitle = content.sections.size > 1)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SectionTopBar(
    component: SectionComponent,
    content: SectionState.Content,
    scrollBehavior: TopAppBarScrollBehavior,
) {

    val isMenuOpenValue = remember { MutableValue(false) }
    val isMenuOpen by isMenuOpenValue.subscribeAsState()

    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton({
                component.navigateBack()
            }) {
                Icon(Icons.Rounded.ArrowBack, "Back")
            }
        },
        title = {
            Text(
                "${content.collection.title}: ${
                    if (content.sections.size == 1) {
                        content.sections[0].title
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

private fun LazyGridScope.section(
    component: SectionComponent,
    content: SectionState.Content,
    section: SectionModel,
    showTitle: Boolean,
) {
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
    val items = content.items[section.id].orEmpty()
    items(
        count = items.size,
        key = { items[it].id },
        span = { GridItemSpan(if (section.id == "basic" || section.id == "voiced") 3 else 5) }
    ) {
        val item = items[it]
        Box(
            modifier = Modifier
        ) {
            if (item.type == CollectionItemType.HIEROGLYPH) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 6.dp),
//                            elevation = CardDefaults.cardElevation(6.dp),
                    onClick = {
//                                component.navigateToItemDetails()
                    }
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