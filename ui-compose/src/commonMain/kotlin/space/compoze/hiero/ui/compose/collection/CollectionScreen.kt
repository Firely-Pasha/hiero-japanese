@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@file:Suppress("NAME_SHADOWING")

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.ui.compose.modal.Anchor
import space.compoze.hiero.ui.compose.modal.HieroModalConsumer
import space.compoze.hiero.ui.compose.popover.showMenu
import space.compoze.hiero.ui.compose.section.randomColor
import space.compoze.hiero.ui.compose.utils.plus
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.collection.component.CollectionComponent
import space.compoze.hiero.ui.shared.collection.store.CollectionStore

@Composable
fun CollectionScreen(component: CollectionComponent) {

    val state by component.state.subscribeAsState()
    when (val state = state) {
        is CollectionStore.State.Error -> CollectionError(
            state = state,
        )

        CollectionStore.State.Loading -> CircularProgressIndicator()
        is CollectionStore.State.Content -> CollectionContent(
            state = state,
            onStartQuiz = component::startQuiz,
            onNavigateToSection = component::navigateToSection
        )
    }
}

@Composable
fun CollectionError(
    state: CollectionStore.State.Error,
) {
    Scaffold {
        Text(state.error.message ?: "ERROR!")
    }
}

private val ListPadding = PaddingValues(16.dp)
private val ListWithFabPadding = PaddingValues(bottom = 128.dp)

@Composable
fun CollectionContent(
    state: CollectionStore.State.Content,
    onStartQuiz: (isBookmarkOnly: Boolean) -> Unit,
    onNavigateToSection: (sectionId: String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
                title = {
                    Text(state.collection.title)
                },
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
            ) {
                if (state.canStartQuiz) {
                    if (state.canStartBookmarkedQuiz) {
                        SmallFloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            onClick = { onStartQuiz(true) }
                        ) {
                            Icon(
                                Icons.Rounded.Bookmark,
                                "Play bookmarked",
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        onClick = { onStartQuiz(false) }
                    ) {
                        Icon(Icons.Rounded.PlayArrow, "Play")
                    }
                }
            }
        }
    ) {
        val containerPadding = it + ListPadding + ListWithFabPadding
        LazyColumn(
            contentPadding = containerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = state.sections,
                key = { it.id }
            ) {
                SectionItem(
                    it,
                    onClick = { onNavigateToSection(it.id) }
                )
            }
        }
    }
}

@Composable
private fun SectionItem(
    section: SectionModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
//            containerColor = randomColor()
        ),
        onClick = onClick,
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
        }
    }
}