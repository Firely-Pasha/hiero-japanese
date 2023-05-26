@file:Suppress("ThrowableNotThrown")

package space.compoze.hiero.ui.shared.collection.store

import arrow.core.None
import arrow.core.Some
import arrow.core.raise.either
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.interactor.CollectionGetById
import space.compoze.hiero.domain.section.interactor.SectionGetOfCollection
import space.compoze.hiero.ui.shared.utils.applyState
import space.compoze.hiero.ui.shared.utils.with

@OptIn(ExperimentalMviKotlinApi::class)
class CollectionStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionGetById: CollectionGetById by inject()
    private val sectionGetOfCollection: SectionGetOfCollection by inject()

    fun create(collectionId: String): CollectionStore =
        object : CollectionStore,
            Store<CollectionStore.Intent, CollectionStore.State, CollectionStore.Label> by storeFactory.create<CollectionStore.Intent, CollectionStore.Action, CollectionStore.Message, CollectionStore.State, CollectionStore.Label>(
                name = "KEK",
                initialState = CollectionStore.State.Loading,
                bootstrapper = coroutineBootstrapper {
                    launch {
                        either {
                            val collection = when (
                                val collection = collectionGetById(collectionId).bind()
                            ) {
                                None -> raise(DomainError("Collection not found :("))
                                is Some -> collection.value
                            }
                            val sections = sectionGetOfCollection(collectionId).bind()
                            withContext(Dispatchers.Main) {
                                dispatch(
                                    CollectionStore.Action.Loaded(
                                        collection = collection,
                                        sections = sections,
                                        previews = listOf()
                                    )
                                )
                            }
                        }.onLeft {
                            withContext(Dispatchers.Main) {
                                dispatch(CollectionStore.Action.LoadingError(it))
                            }
                        }
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<CollectionStore.Action.LoadingError> {
                        dispatch(CollectionStore.Message.Error(it.error))
                    }
                    onAction<CollectionStore.Action.Loaded> {
                        dispatch(
                            CollectionStore.Message.InitCollection(
                                collection = it.collection,
                                sections = it.sections,
                                previews = it.previews
                            )
                        )
                        launch {
                            state.with { content: CollectionStore.State.Content ->
                                sectionGetOfCollection.flow(content.collection.id)
                                    .collect { sections ->
                                        withContext(Dispatchers.Main) {
                                            dispatch(
                                                CollectionStore.Message.SetSections(
                                                    sections = sections,
                                                    previews = listOf()
                                                )
                                            )
                                        }
                                    }
                            }
                        }
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is CollectionStore.Message.Error -> CollectionStore.State.Error(msg.error)
                        is CollectionStore.Message.InitCollection -> CollectionStore.State.Content(
                            collection = msg.collection,
                            sections = msg.sections,
                            previews = msg.previews
                        )

                        is CollectionStore.Message.SetCollection -> applyState { content: CollectionStore.State.Content ->
                            content.copy(collection = msg.collection)
                        }

                        is CollectionStore.Message.SetSections -> applyState { content: CollectionStore.State.Content ->
                            content.copy(
                                sections = msg.sections,
                                previews = msg.previews
                            )
                        }

                    }
                },
            ) {}
}