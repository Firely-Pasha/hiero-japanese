@file:Suppress("ThrowableNotThrown")

package space.compoze.hiero.ui.shared.collection

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
import space.compoze.hiero.domain.section.interactor.SectionGetOfCollectionUseCase
import space.compoze.hiero.domain.section.model.SectionModel

@OptIn(ExperimentalMviKotlinApi::class)
class CollectionStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionGetByUuidUseCase: CollectionGetByUuidUseCase by inject()
    private val sectionGetOfCollectionUseCase: SectionGetOfCollectionUseCase by inject()
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
                    val collectionSections = sectionGetOfCollectionUseCase(collectionId).bind()
                    val collectionItems = collectionItemGetOfCompositionUseCase(collectionId).bind()
                    dispatch(
                        CollectionAction.Loaded(
                            collection = collection,
                            sections = collectionSections
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
                            sections = it.sections
                        )
                    )
                }
                onIntent<CollectionIntent.AddSection> {
                    state.withContent { state ->
                        dispatch(
                            CollectionMessage.AddSection(
                                SectionModel(
                                    id = "${state.sections.size.toLong() + 1000L}",
                                    collectionId = "",
                                    title = "Item ${state.sections.size}"
//                                    id = state.sections.size.toLong() + 1000L,
//                                    sectionId = "KEK",
//                                    type = "word",
//                                    value = "Item ${state.items.size}",
//                                    transcription = "",
//                                    sort = 1L,
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
                        sections = msg.sections
                    )

                    is CollectionMessage.SetCollection -> applyContent {
                        copy(collection = msg.collection)
                    }

                    is CollectionMessage.AddSection -> applyContent {
                        copy(sections = sections + msg.section)
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