@file:Suppress("ThrowableNotThrown")

package space.compoze.hiero.ui.shared.section.state

import arrow.core.Option
import arrow.core.Some
import arrow.core.flatten
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.core.some
import arrow.core.toOption
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.interactor.CollectionGetById
import space.compoze.hiero.domain.collectionitem.CollectionItemNotification
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfSection
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateById
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateBySectionId
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationGetFlow
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData
import space.compoze.hiero.domain.section.interactor.SectionGetById
import space.compoze.hiero.domain.section.interactor.SectionGetOfCollection
import space.compoze.hiero.domain.variant.VariantGetOfCollection
import space.compoze.hiero.ui.shared.utils.applyState
import space.compoze.hiero.ui.shared.utils.with

@OptIn(ExperimentalMviKotlinApi::class)
class SectionStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val dispatchers: AppDispatchers by inject()

    private val collectionGetById: CollectionGetById by inject()
    private val sectionGetById: SectionGetById by inject()
    private val collectionItemGetOfSection: CollectionItemGetOfSection by inject()
    private val collectionItemUpdateById: CollectionItemUpdateById by inject()
    private val collectionItemUpdateBySectionId: CollectionItemUpdateBySectionId by inject()
    private val collectionItemNotificationGetFlow: CollectionItemNotificationGetFlow by inject()
    private val variantGetOfCollection: VariantGetOfCollection by inject()

    fun create(
        sectionId: String,
        collectionId: String? = null,
    ): SectionStore =
        object : SectionStore,
            Store<SectionStore.Intent, SectionStore.State, Nothing> by storeFactory.create<SectionStore.Intent, SectionStore.Action, SectionStore.Message, SectionStore.State, Nothing>(
                name = "KEK",
                initialState = SectionStore.State.Loading,
                bootstrapper = coroutineBootstrapper(dispatchers.main) {
                    either {
                        val section = sectionGetById(sectionId).bind()
                            .getOrElse { raise(DomainError("Section not found :(")) }
                        val collection = collectionGetById(section.collectionId).bind()
                            .getOrElse { raise(DomainError("Collection not found :(")) }
                        val sectionItems = collectionItemGetOfSection(sectionId).bind()
                        val variants = variantGetOfCollection(collection).bind()
                        dispatch(
                            SectionStore.Action.Loaded(
                                collection = collection,
                                section = section,
                                variants = variants,
                                items = sectionItems
                            )
                        )
                    }.onLeft {
                        dispatch(SectionStore.Action.LoadingError(it))
                    }
                },
                executorFactory = coroutineExecutorFactory(dispatchers.main) {
                    onAction<SectionStore.Action.LoadingError> {
                        dispatch(SectionStore.Message.Error(it.error))
                    }
                    onAction<SectionStore.Action.Loaded> {
                        dispatch(
                            SectionStore.Message.Init(
                                collection = it.collection,
                                section = it.section,
                                primaryVariant = it.variants.find { it.type == 1L }!!,
                                secondaryVariant = it.variants.find { it.type == 2L }!!,
                                items = it.items,
                            )
                        )
                        state.with { content: SectionStore.State.Content ->
                            launch {
                                collectionItemNotificationGetFlow().collect {
                                    if (it !is CollectionItemNotification.Changed) return@collect
                                    state.with { content: SectionStore.State.Content ->
                                        if (it.new.sectionId == content.section.id && !content.items.contains(it.new)) {
                                            withContext(dispatchers.main) {
                                                dispatch(SectionStore.Message.SetItems(listOf(it.new)))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.ToggleItemSelect> { intent ->
                        state.with { content: SectionStore.State.Content ->
                            either {
                                content.getItem(intent.itemId)
                                    .onSome { item ->
                                        val result = collectionItemUpdateById(
                                            intent.itemId, CollectionItemMutationData(
                                                isSelected = Some(!item.isSelected && !item.isBookmarked),
                                                isBookmarked = Some(false)
                                            )
                                        ).bind()
                                        dispatch(SectionStore.Message.SetItems(listOf(result)))
                                    }
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.ToggleItemBookmark> { intent ->
                        state.with { content: SectionStore.State.Content ->
                            either {
                                content.getItem(intent.itemId)
                                    .onSome { item ->
                                        collectionItemUpdateById(
                                            item.id,
                                            CollectionItemMutationData(
                                                isBookmarked = Some(!item.isBookmarked),
                                                isSelected = Some(false),
                                            ),
                                        ).bind()
                                    }
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.SelectAll> {
                        state.with { content: SectionStore.State.Content ->
                            either {
                                val items = collectionItemUpdateBySectionId(
                                    content.section.id, CollectionItemMutationData(
                                        isSelected = true.some(),
                                    )
                                ).bind()
                                dispatch(SectionStore.Message.SetItems(items))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.ClearAll> {
                        state.with { content: SectionStore.State.Content ->
                            either {
                                val items = collectionItemUpdateBySectionId(
                                    content.section.id, CollectionItemMutationData(
                                        isSelected = false.some()
                                    )
                                ).bind()
                                dispatch(SectionStore.Message.SetItems(items))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.ToggleItemAndSetSelectMode> { intent ->
                        state.with { content: SectionStore.State.Content ->
                            either {
                                val item = content.items
                                    .find { item -> item.id == intent.itemId }
                                    ?: return@either
                                val selectionMode = !item.isSelected
                                collectionItemUpdateById(
                                    intent.itemId,
                                    CollectionItemMutationData(
                                        isSelected = Some(selectionMode),
                                    ),
                                ).bind()
                                dispatch(
                                    SectionStore.Message.SetSelectionMode(
                                        selectionMode = selectionMode,
                                    )
                                )
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                    onIntent<SectionStore.Intent.ToggleItemBySelect> { intent ->
                        state.with { content: SectionStore.State.Content ->
                            either {
                                collectionItemUpdateById(
                                    intent.itemId,
                                    CollectionItemMutationData(
                                        isSelected = Some(content.selectionMode),
                                    ),
                                ).bind()
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
                            section = msg.section,
                            primaryVariant = msg.primaryVariant,
                            secondaryVariant = msg.secondaryVariant,
                            items = msg.items,
                            selectionMode = true
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
                        is SectionStore.Message.SetSelectionMode -> applyState { content: SectionStore.State.Content ->
                            content.copy(
                                selectionMode = msg.selectionMode
                            )
                        }
                    }
                }
            ) {}
}

private fun SectionStore.State.Content.getItem(itemId: String): Option<CollectionItemModel> {
    return items.find { item -> item.id == itemId }.toOption()
}