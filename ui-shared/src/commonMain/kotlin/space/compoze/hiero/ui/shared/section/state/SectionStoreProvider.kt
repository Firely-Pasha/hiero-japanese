@file:Suppress("ThrowableNotThrown")

package space.compoze.hiero.ui.shared.section.state

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.interactor.CollectionGetById
import space.compoze.hiero.domain.collectionitem.CollectionItemNotification
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfSection
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateByIdUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateBySectionId
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationGetFlowUseCase
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData
import space.compoze.hiero.domain.section.interactor.SectionGetByIdUseCase
import space.compoze.hiero.domain.section.interactor.SectionGetOfCollection
import space.compoze.hiero.ui.shared.utils.applyState
import space.compoze.hiero.ui.shared.utils.with

@OptIn(ExperimentalMviKotlinApi::class)
class SectionStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionGetById: CollectionGetById by inject()
    private val sectionGetByIdUseCase: SectionGetByIdUseCase by inject()
    private val sectionGetOfCollection: SectionGetOfCollection by inject()
    private val collectionItemGetOfSection: CollectionItemGetOfSection by inject()
    private val collectionItemUpdateByIdUseCase: CollectionItemUpdateByIdUseCase by inject()
    private val collectionItemUpdateBySectionId: CollectionItemUpdateBySectionId by inject()
    private val collectionItemNotificationGetFlowUseCase: CollectionItemNotificationGetFlowUseCase by inject()

    fun create(
        scope: CoroutineScope,
        sectionId: String,
        collectionId: String? = null,
    ): SectionStore =
        object : SectionStore,
            Store<SectionStore.Intent, SectionStore.State, Nothing> by storeFactory.create<SectionStore.Intent, SectionStore.Action, SectionStore.Message, SectionStore.State, Nothing>(
                name = "KEK",
                initialState = SectionStore.State.Loading,
                bootstrapper = coroutineBootstrapper {
                    either {
                        if (collectionId != null) {
                            val collection = collectionGetById(collectionId).bind()
                                .getOrElse { raise(DomainError("Collection not found :(")) }
                            val sections = sectionGetOfCollection(collectionId).bind()
                            val sectionItems = collectionItemGetOfSection(
                                sections.map { it.id }
                            ).bind()
                            dispatch(
                                SectionStore.Action.Loaded(
                                    collection = collection,
                                    sections = sections,
                                    items = sectionItems.values.flatten()
                                )
                            )
                            return@coroutineBootstrapper
                        }
                        val section = sectionGetByIdUseCase(sectionId).bind()
                            .getOrElse { raise(DomainError("Section not found :(")) }
                        val collection = collectionGetById(section.collectionId).bind()
                            .getOrElse { raise(DomainError("Collection not found :(")) }
                        val sectionItems = collectionItemGetOfSection(sectionId).bind()
                        dispatch(
                            SectionStore.Action.Loaded(
                                collection = collection,
                                sections = listOf(section),
                                items = sectionItems
                            )
                        )
                    }.onLeft {
                        dispatch(SectionStore.Action.LoadingError(it))
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<SectionStore.Action.LoadingError> {
                        dispatch(SectionStore.Message.Error(it.error))
                    }
                    onAction<SectionStore.Action.Loaded> {
                        dispatch(
                            SectionStore.Message.Init(
                                collection = it.collection,
                                sections = it.sections,
                                items = it.items,
                            )
                        )
                        state.with { content: SectionStore.State.Content ->
                            collectionItemNotificationGetFlowUseCase().onEach {
                                when (it) {
                                    is CollectionItemNotification.Changed -> {
                                        if (it.new.sectionId in content.sections.map { it.id }) {
                                            withContext(Dispatchers.Main) {
                                                dispatch(SectionStore.Message.SetItems(listOf(it.new)))
                                            }
                                        }
                                    }
                                }
                            }.launchIn(scope)
                        }
                    }
                    onIntent<SectionStore.Intent.ToggleItemSelect> { intent ->
                        state.with { content: SectionStore.State.Content ->
                            either {
                                val result = collectionItemUpdateByIdUseCase(
                                    intent.itemId, CollectionItemMutationData(
                                        isSelected = Some(
                                            !(content.items.find { item -> item.id == intent.itemId }?.isSelected
                                                ?: false),
                                        ),
                                        isBookmarked = Some(false)
                                    )
                                ).bind()
                                dispatch(SectionStore.Message.SetItems(listOf(result)))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.ToggleItemBookmark> { intent ->
                        state.with { content: SectionStore.State.Content ->
                            either {
                                val item = content.items.find { item -> item.id == intent.itemId }
                                        ?: return@either
                                collectionItemUpdateByIdUseCase(
                                    intent.itemId,
                                    CollectionItemMutationData(
                                        isBookmarked = Some(!item.isBookmarked),
                                        isSelected = Some(!item.isBookmarked),
                                    ),
                                ).bind()
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.SelectAll> {
                        state.with { content: SectionStore.State.Content ->
                            either {
                                val sectionItems = content.sections.map {
                                    collectionItemUpdateBySectionId(
                                        it.id, CollectionItemMutationData(
                                            isSelected = true.some()
                                        )
                                    ).bind()
                                }.flatten()
                                dispatch(SectionStore.Message.SetItems(sectionItems))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.ClearAll> {
                        state.with { content: SectionStore.State.Content ->
                            either {
                                val sectionItems = content.sections.map {
                                    collectionItemUpdateBySectionId(
                                        it.id, CollectionItemMutationData(
                                            isSelected = false.some()
                                        )
                                    ).bind()
                                }.flatten()
                                dispatch(SectionStore.Message.SetItems(sectionItems))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is SectionStore.Message.Error -> SectionStore.State.Error(msg.error)
                        is SectionStore.Message.Init -> SectionStore.State.Content(
                            collection = msg.collection,
                            sections = msg.sections,
                            items = msg.items,
                        )

                        is SectionStore.Message.SelectItem -> applyState { content: SectionStore.State.Content ->
                            val index = content.items.indexOfFirst { it.id == msg.itemId }
                            val mutableList = content.items.toMutableList()
                            mutableList[index] = content.items[index].copy(
                                isSelected = msg.value
                            )
                            content.copy(
                                items = mutableList
                            )
                        }

                        is SectionStore.Message.SetItems -> applyState { content: SectionStore.State.Content ->
                            content.copy(
                                items = content.items.toMutableList().apply {
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
}