@file:Suppress("ThrowableNotThrown")

package space.compoze.hiero.ui.shared.collection

import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import arrow.core.raise.either
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.interactor.CollectionGetByUuidUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfCompositionUseCase
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel
import kotlin.random.Random

@OptIn(ExperimentalMviKotlinApi::class)
class CollectionStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionGetByUuidUseCase: CollectionGetByUuidUseCase by inject()
    private val collectionItemGetOfCompositionUseCase: CollectionItemGetOfCompositionUseCase by inject()

    fun create(collectionId: String): CollectionStore = object : CollectionStore,
        Store<CollectionIntent, CollectionState, CollectionLabel> by storeFactory.create<CollectionIntent, CollectionAction, CollectionMessage, CollectionState, CollectionLabel>(
            name = "KEK",
            initialState = CollectionState.Loading,
            bootstrapper = coroutineBootstrapper {
                either {
                    val collection =
                        when (val collection = collectionGetByUuidUseCase(collectionId).bind()) {
                            None -> raise(DomainError("Collection not found :("))
                            is Some -> collection.value
                        }
                    val collectionItems = collectionItemGetOfCompositionUseCase(collectionId).bind()
                    dispatch(
                        CollectionAction.Loaded(
                            collection = collection,
                            items = collectionItems
                        )
                    )
                }.onLeft {
                    dispatch(CollectionAction.LoadingError(it))
                }
            },
            executorFactory = coroutineExecutorFactory {
                onAction<CollectionAction.LoadingError> {
                    dispatch(CollectionMessage.Error(it.error))
                }
                onAction<CollectionAction.Loaded> {
                    dispatch(
                        CollectionMessage.InitCollection(
                            collection = it.collection,
                            items = it.items
                        )
                    )
                }
                onIntent<CollectionIntent.AddItem> {
                    state.withContent { state ->
                        dispatch(
                            CollectionMessage.AddItem(
                                CollectionItemModel(
                                    id = state.items.size.toLong() + 1000L,
                                    collectionId = "KEK",
                                    type = "word",
                                    value = "Item ${state.items.size}",
                                    transcription = "",
                                    sort = 1L,
                                )
                            )
                        )
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    is CollectionMessage.Error -> CollectionState.Error(msg.error)
                    is CollectionMessage.InitCollection -> CollectionState.Content(
                        collection = msg.collection,
                        items = msg.items
                    )

                    is CollectionMessage.SetCollection -> applyContent {
                        copy(collection = msg.collection)
                    }

                    is CollectionMessage.AddItem -> applyContent {
                        copy(items = items + msg.item)
                    }
                }
            }
        ) {}

    private fun CollectionState.withContent(block: (CollectionState.Content) -> Unit) {
        if (this is CollectionState.Content) {
            block(this)
        }
    }

    private fun CollectionState.applyContent(block: CollectionState.Content.() -> CollectionState): CollectionState {
        if (this is CollectionState.Content) {
            return block(this)
        }
        return this
    }
}