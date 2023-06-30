@file:OptIn(ExperimentalMviKotlinApi::class)

package space.compoze.hiero.ui.shared.quiz.store

import arrow.core.raise.either
import arrow.core.some
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetByIds
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateById
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData
import space.compoze.hiero.ui.shared.utils.with

class QuizStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionItemGetByIds: CollectionItemGetByIds by inject()
    private val collectionItemUpdateById: CollectionItemUpdateById by inject()

    fun create(items: List<String>): QuizStore =
        object : QuizStore,
            Store<QuizStore.Intent, QuizStore.State, Nothing> by storeFactory.create<QuizStore.Intent, QuizStore.Action, QuizStore.Message, QuizStore.State, Nothing>(
                name = "quiz_store",
                initialState = QuizStore.State.Loading,
                bootstrapper = coroutineBootstrapper {
                    either {
                        val result = collectionItemGetByIds(items).bind()
                        dispatch(
                            QuizStore.Action.Loaded(
                                items = result,
                            )
                        )
                    }.onLeft {
                        dispatch(QuizStore.Action.LoadingError(it))
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<QuizStore.Action.Loaded> {
                        val itemPull = it.items.toTypedArray().also { it.shuffle() }.toList()
                        dispatch(
                            QuizStore.Message.Init(
                                items = it.items,
                                currentItem = itemPull.first(),
                                itemPull = itemPull.takeLast(itemPull.size - 1).ifEmpty { it.items }
                            )
                        )
                    }
                    onIntent<QuizStore.Intent.NextItem> {
                        state.with { content: QuizStore.State.Content ->
                            val nextItem = content.itemPool
                                .find { it.id != content.currentItem.id }
                                ?: content.itemPool.first()
                            val nextItemPull = content.itemPool
                                .filter { it.id != nextItem.id }
                            dispatch(QuizStore.Message.SetCurrentItem(nextItem))
                            dispatch(QuizStore.Message.SetItemPull(nextItemPull))
                        }
                        state.with { content: QuizStore.State.Content ->
                            if (content.itemPool.isEmpty()) {
                                val itemPull =
                                    content.items.toTypedArray().also { it.shuffle() }.toList()
                                dispatch(QuizStore.Message.SetItemPull(itemPull))
                            }
                        }
                    }
                    onIntent<QuizStore.Intent.BookmarkCurrentItem> {
                        state.with { content: QuizStore.State.Content ->
                            either {
                                val result = collectionItemUpdateById(
                                    content.currentItem.id,
                                    CollectionItemMutationData(
                                        isBookmarked = content.currentItem.isBookmarked.some()
                                            .map { !it }
                                    )
                                ).bind()
                                dispatch(QuizStore.Message.SetCurrentItem(result))
                                dispatch(QuizStore.Message.SetItem(result))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                },
                reducer = {
                    when {
                        it is QuizStore.Message.Init -> QuizStore.State.Content(
                            items = it.items,
                            currentItem = it.currentItem,
                            itemPool = it.itemPull,
                        )

                        this is QuizStore.State.Content -> when (val msg = it) {
                            is QuizStore.Message.Init -> this
                            is QuizStore.Message.SetCurrentItem -> copy(
                                currentItem = msg.item
                            )

                            is QuizStore.Message.SetItemPull -> {
                                copy(
                                    itemPool = msg.itemPull
                                )
                            }

                            is QuizStore.Message.SetItem -> copy(
                                items = this.items.toMutableList().apply {
                                    val index = indexOfFirst { it.id == msg.item.id }
                                    set(index, msg.item)
                                }.toList()
                            )
                        }

                        else -> this
                    }
                }
            ) {}

}