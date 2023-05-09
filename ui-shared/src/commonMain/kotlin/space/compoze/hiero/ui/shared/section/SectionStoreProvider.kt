@file:Suppress("ThrowableNotThrown")

package space.compoze.hiero.ui.shared.section

import arrow.core.Some
import arrow.core.flatten
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.core.some
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.interactor.CollectionGetByUuidUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfSectionUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateByIdUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateBySectionId
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData
import space.compoze.hiero.domain.section.interactor.SectionGetByIdUseCase
import space.compoze.hiero.domain.section.interactor.SectionGetOfCollectionUseCase

@OptIn(ExperimentalMviKotlinApi::class)
class SectionStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionGetByUuidUseCase: CollectionGetByUuidUseCase by inject()
    private val sectionGetByIdUseCase: SectionGetByIdUseCase by inject()
    private val sectionGetOfCollectionUseCase: SectionGetOfCollectionUseCase by inject()
    private val collectionItemGetOfSectionUseCase: CollectionItemGetOfSectionUseCase by inject()
    private val collectionItemUpdateByIdUseCase: CollectionItemUpdateByIdUseCase by inject()
    private val collectionItemUpdateBySectionId: CollectionItemUpdateBySectionId by inject()

    fun create(sectionId: String, collectionId: String? = null): SectionStore =
        object : SectionStore,
            Store<SectionIntent, SectionState, Nothing> by storeFactory.create<SectionIntent, SectionAction, SectionMessage, SectionState, Nothing>(
                name = "KEK",
                initialState = SectionState.Loading,
                bootstrapper = coroutineBootstrapper {
                    either {
                        if (collectionId != null) {
                            val collection = collectionGetByUuidUseCase(collectionId).bind()
                                .getOrElse { raise(DomainError("Collection not found :(")) }
                            val sections = sectionGetOfCollectionUseCase(collectionId).bind()
                            val sectionItems = collectionItemGetOfSectionUseCase(
                                sections.map { it.id }
                            ).bind()
                            dispatch(
                                SectionAction.Loaded(
                                    collection = collection,
                                    sections = sections,
                                    items = sectionItems.values.flatten()
                                )
                            )
                            return@coroutineBootstrapper
                        }
                        val section = sectionGetByIdUseCase(sectionId).bind()
                            .getOrElse { raise(DomainError("Section not found :(")) }
                        val collection = collectionGetByUuidUseCase(section.collectionId).bind()
                            .getOrElse { raise(DomainError("Collection not found :(")) }
                        val sectionItems = collectionItemGetOfSectionUseCase(sectionId).bind()
                        dispatch(
                            SectionAction.Loaded(
                                collection = collection,
                                sections = listOf(section),
                                items = sectionItems
                            )
                        )
                    }.onLeft {
                        dispatch(SectionAction.LoadingError(it))
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<SectionAction.LoadingError> {
                        dispatch(SectionMessage.Error(it.error))
                    }
                    onAction<SectionAction.Loaded> {
                        dispatch(
                            SectionMessage.InitSection(
                                collection = it.collection,
                                sections = it.sections,
                                items = it.items,
                            )
                        )
                    }
                    onIntent<SectionIntent.ToggleItemSelect> { intent ->
                        state.withState<SectionState.Content> {
                            either {
                                val result = collectionItemUpdateByIdUseCase(
                                    intent.itemId, CollectionItemMutationData(
                                        isSelected = Some(
                                            !(items.find { item -> item.id == intent.itemId }?.isSelected
                                                ?: false)
                                        )
                                    )
                                ).bind()
                                dispatch(SectionMessage.SetItems(listOf(result)))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionIntent.SelectAll> {
                        state.withState<SectionState.Content> {
                            either {
                                val sectionItems = sections.map {
                                    collectionItemUpdateBySectionId(
                                        it.id, CollectionItemMutationData(
                                            isSelected = true.some()
                                        )
                                    ).bind()
                                }.flatten()
                                dispatch(SectionMessage.SetItems(sectionItems))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionIntent.ClearAll> {
                        state.withState<SectionState.Content> {
                            either {
                                val sectionItems = sections.map {
                                    collectionItemUpdateBySectionId(
                                        it.id, CollectionItemMutationData(
                                            isSelected = false.some()
                                        )
                                    ).bind()
                                }.flatten()
                                dispatch(SectionMessage.SetItems(sectionItems))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is SectionMessage.Error -> SectionState.Error(msg.error)
                        is SectionMessage.InitSection -> SectionState.Content(
                            collection = msg.collection,
                            sections = msg.sections,
                            items = msg.items,
                        )

                        is SectionMessage.SelectItem -> applyState<SectionState.Content> {
                            val index = items.indexOfFirst { it.id == msg.itemId }
                            val mutableList = items.toMutableList()
                            mutableList[index] = items[index].copy(
                                isSelected = msg.value
                            )
                            copy(
                                items = mutableList
                            )
                        }

                        is SectionMessage.SetItems -> applyState<SectionState.Content> {
                            copy(
                                items = items.toMutableList().apply {
                                    msg.items.forEach { msgItem ->
                                        val index = indexOfFirst { it.id == msgItem.id }
                                        set(index, msgItem)
                                    }
                                }.toList()
                            )
                        }
                    }
                }
            ) {}

    private inline fun <reified State : SectionState> SectionState.withState(block: State.() -> Unit) {
        if (this is State) {
            block(this)
        }
    }

    private inline fun <reified State : SectionState> SectionState.applyState(block: State.() -> SectionState): SectionState {
        if (this is State) {
            return block(this)
        }
        return this
    }
}