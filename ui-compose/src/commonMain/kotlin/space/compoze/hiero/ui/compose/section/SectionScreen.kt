@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("NAME_SHADOWING")

package space.compoze.hiero.ui.compose.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    Text("${content.collection.title}: ${
                        if (content.sections.size == 1) {
                            content.sections[0].title
                        } else {
                            "All"
                        }
                    }")
                },
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

private fun LazyGridScope.section(
    component: SectionComponent,
    content: SectionState.Content,
    section: SectionModel,
    showTitle: Boolean
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