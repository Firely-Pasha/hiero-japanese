package space.compoze.hiero.ui.shared.collection

import com.arkivanov.mvikotlin.core.store.Store
import kotlin.random.Random



data class CollectionState(
    val collectionId: String,
    val value: Long = 0L,
    val items: List<String> = buildList {
        repeat(Random.nextInt(20, 50)) {
            add("Item $it")
        }
    }
)

sealed interface CollectionIntent {
    object AddItem : CollectionIntent
}

sealed interface CollectionMessage {
    data class AddItem(val item: String) : CollectionMessage
}

sealed interface CollectionLabel {
    data class ShowMessage(val message: String) : CollectionLabel
}

interface CollectionStore : Store<CollectionIntent, CollectionState, CollectionLabel>