@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@file:Suppress("NAME_SHADOWING")

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import space.compoze.hiero.domain.collectionitem.enums.CollectionItemType
import space.compoze.hiero.domain.section.model.SectionModel
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
            item {
                SectionItem(CollectionComponent.AllSection) {
                    component.navigateToSection(CollectionComponent.AllSection)
                }
            }
            items(
                count = state.sections.size,
                key = { state.sections[it].id }
            ) {
                val section = state.sections[it]
                SectionItem(section) { component.navigateToSection(section) }
            }
//            items(
//                count = state.items.size,
//                key = { state.items[it].id }
//            ) {
//                val item = state.items[it]
//                Box(
//                    modifier = Modifier
//                ) {
//                    if (item.type == CollectionItemType.HIEROGLYPH) {
//                        Card(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(vertical = 6.dp),
//                            elevation = CardDefaults.cardElevation(6.dp),
//                            onClick = {
//                                component.navigateToItemDetails()
//                            }
//                        ) {
//                            Box(
//                                modifier = Modifier.padding(6.dp)
//                                    .align(Alignment.CenterHorizontally),
//                            ) {
//                                Column(
//                                    horizontalAlignment = Alignment.CenterHorizontally
//                                ) {
//                                    Text(
//                                        item.value,
//                                        fontSize = 32.sp
//                                    )
//                                    Text(
//                                        item.transcription,
//                                        fontSize = 16.sp
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
        }
    }
}

@Composable
private fun SectionItem(section: SectionModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
//                            Text("Start")
//                            Text("End")
            Text(
                section.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Box {
                Icon(Icons.Default.ArrowForward, "Show")
            }
        }
    }
}