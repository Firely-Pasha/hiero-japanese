@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("NAME_SHADOWING")

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                    Text(state.collection.title)
                },
                actions = {
                    IconButton({
                        component.addItem()
                    }) {
                        Icon(Icons.Default.Add, "Add item")
                    }
                }
            )
        }
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(4.dp),
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(state.items.size) {
                val item = state.items[it]
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            component.navigateToItemDetails()
                        },
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(6.dp),
                    ) {
                        Text(item)
                    }
                }
            }
        }
    }
}