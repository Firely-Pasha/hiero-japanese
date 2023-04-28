@file:OptIn(ExperimentalMaterialApi::class)
@file:Suppress("NAME_SHADOWING")

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
                        .padding(vertical = 6.dp),
                    elevation = 6.dp,
                    onClick = {
                        component.navigateToItemDetails()
                    }
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