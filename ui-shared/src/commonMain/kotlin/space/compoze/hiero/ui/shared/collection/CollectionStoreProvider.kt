package space.compoze.hiero.ui.shared.collection

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

@OptIn(ExperimentalMviKotlinApi::class)
class CollectionStoreProvider(private val storeFactory: StoreFactory) {
    fun create(collectionId: String): CollectionStore = object : CollectionStore,
        Store<CollectionIntent, CollectionState, CollectionLabel> by storeFactory.create<CollectionIntent, Nothing, CollectionMessage, CollectionState, CollectionLabel>(
            name = "KEK",
            initialState = CollectionState(
                collectionId = collectionId
            ),
            executorFactory = coroutineExecutorFactory {
                onIntent<CollectionIntent.AddItem> {
                    dispatch(CollectionMessage.AddItem("Item ${state.items.size}"))
                }
            },
            reducer = { msg ->
                when (msg) {
                    is CollectionMessage.AddItem -> copy(
                        items = items + msg.item
                    )
                }
            }
        ) {}
}