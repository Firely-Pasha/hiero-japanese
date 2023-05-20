@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@file:Suppress("NAME_SHADOWING")

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.outlined.BookOnline
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.CropPortrait
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.domain.sectionpreview.model.data.SectionPreview
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.collection.CollectionComponent
import space.compoze.hiero.ui.shared.collection.CollectionState

@Composable
fun CollectionScreen(component: CollectionComponent) {

    val state by component.state.subscribeAsState()
    println("RERENDER")
    when (val state = state) {
        is CollectionState.Error -> CollectionError(component, state)
        CollectionState.Loading -> CircularProgressIndicator()
        is CollectionState.Content -> CollectionContent(component, state)
    }
}

@Composable
fun CollectionError(component: CollectionComponent, state: CollectionState.Error) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            component.navigateBack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, "Add item")
                    }
                },
                title = {
                }
            )
        }
    ) {
        Text(state.error.message ?: "ERROR!")
    }
}

@Composable
fun CollectionContent(component: CollectionComponent, state: CollectionState.Content) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(state.collection.title)
                },
            )
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
        LazyColumn(
            contentPadding = containerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            item {
//                SectionItem(CollectionComponent.AllSection, None) {
//                    component.navigateToSection(CollectionComponent.AllSection)
//                }
//            }
            items(
                count = state.sections.size,
                key = { state.sections[it].id }
            ) {
                val section = state.sections[it]
                val preview = state.previews.getOrElse(it) { None }
                SectionItem(section, preview) { component.navigateToSection(section) }
            }
        }
    }
}

@Composable
private fun SectionItem(
    section: SectionModel,
    preview: Option<SectionPreview>,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        section.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Rounded.ChevronRight, "Show")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
//                        modifier = Modifier
//                            .background(
//                                color = MaterialTheme.colorScheme.secondaryContainer,
//                                shape = RoundedCornerShape(99.dp)
//                            )
//                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.CropPortrait,
                            "Learnt",
                            modifier = Modifier.height(18.dp),
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("${section.itemsCount}", fontWeight = FontWeight.SemiBold)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                    ) {
                        Row(
//                            modifier = Modifier
//                                .background(
//                                    color = MaterialTheme.colorScheme.secondaryContainer,
//                                    shape = RoundedCornerShape(99.dp)
//                                )
//                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.School,
                                "Learnt",
                                modifier = Modifier.height(18.dp),
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("${section.selectedCount}", fontWeight = FontWeight.SemiBold)
                        }

                        Row(
//                            modifier = Modifier
//                                .background(
//                                    color = MaterialTheme.colorScheme.secondaryContainer,
//                                    shape = RoundedCornerShape(99.dp)
//                                )
//                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.BookmarkBorder,
                                "Learnt",
                                modifier = Modifier.height(18.dp),
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("${section.bookmarkedCount}", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            when (preview) {
                None -> {}
                is Some -> when (val sectionPreview = preview.value) {
                    is SectionPreview.Generated -> {
//                            FlowRow(
//                                modifier = Modifier
//                                    .height(with(LocalDensity.current) {
//                                        24.sp.toDp()
//                                    } + 8.dp)
//                                    .fillMaxWidth()
//                                    .clip(RectangleShape),
//                                horizontalArrangement = Arrangement.SpaceBetween,
//                            ) {
//                                sectionPreview.items.forEach {
//                                    Text(
//                                        text = it.value,
//                                        fontSize = 24.sp,
//                                        softWrap = false
//                                    )
//                                }
//                            }
                    }
                }
            }
        }
    }
}