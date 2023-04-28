package space.compoze.hiero.ui.shared.collection

import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.interactor.CollectionGetByUuidUseCase
import kotlin.random.Random

@OptIn(ExperimentalMviKotlinApi::class)
class CollectionStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionGetByUuidUseCase: CollectionGetByUuidUseCase by inject()

    fun create(collectionId: String): CollectionStore = object : CollectionStore,
        Store<CollectionIntent, CollectionState, CollectionLabel> by storeFactory.create<CollectionIntent, CollectionAction, CollectionMessage, CollectionState, CollectionLabel>(
            name = "KEK",
            initialState = CollectionState.Loading,
            bootstrapper = coroutineBootstrapper {
                when (val result = collectionGetByUuidUseCase(collectionId)) {
                    is Either.Left -> dispatch(
                        CollectionAction.LoadingError(result.value)
                    )

                    is Either.Right -> when (val collection = result.value) {
                        None -> dispatch(
                            CollectionAction.LoadingError(
                                DomainError("Collection not found :("),
                            )
                        )

                        is Some -> dispatch(
                            CollectionAction.Loaded(
                                collection = collection.value
                            )
                        )
                    }
                }
            },
            executorFactory = coroutineExecutorFactory {
                onAction<CollectionAction.LoadingError> {
                    dispatch(CollectionMessage.Error(it.error))
                }
                onAction<CollectionAction.Loaded> {
                    dispatch(CollectionMessage.SetCollection(it.collection))
                }
                onIntent<CollectionIntent.AddItem> {
                    state.withContent { state ->
                        dispatch(CollectionMessage.AddItem("Item ${state.items.size}"))
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    is CollectionMessage.Error -> CollectionState.Error(msg.error)
                    is CollectionMessage.SetCollection -> CollectionState.Content(
                        msg.collection,
                        items = buildList {
                            repeat(Random.nextInt(20, 50)) {
                                add("Item $it")
                            }
                        })

                    is CollectionMessage.AddItem -> applyContent {
                        copy(
                            items = items + msg.item
                        )
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