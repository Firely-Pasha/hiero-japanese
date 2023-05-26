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
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateByIdUseCase
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData
import space.compoze.hiero.ui.shared.utils.with

class QuizStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionItemGetByIds: CollectionItemGetByIds by inject()
    private val collectionItemUpdateByIdUseCase: CollectionItemUpdateByIdUseCase by inject()

    fun create(items: List<Long>): QuizStore =
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
                        dispatch(
                            QuizStore.Message.Init(
                                items = it.items,
                                currentItem = it.items.random()
                            )
                        )
                    }
                    onIntent<QuizStore.Intent.NextItem> {
                        state.with { content: QuizStore.State.Content ->
                            val isSingleSame = content.items.all { it == content.currentItem }
                            val nextItem = if (isSingleSame) {
                                content.currentItem
                            } else {
                                var randomItem = content.items.random()
                                while (randomItem == content.currentItem) {
                                    randomItem = content.items.random()
                                }
                                randomItem
                            }
                            dispatch(QuizStore.Message.ChangeCurrentItem(item = nextItem))
                        }
                    }
                    onIntent<QuizStore.Intent.BookmarkCurrentItem> {
                        state.with { content: QuizStore.State.Content ->
                            either {
                                val result = collectionItemUpdateByIdUseCase(
                                    content.currentItem.id,
                                    CollectionItemMutationData(
                                        isBookmarked = content.currentItem.isBookmarked.some()
                                            .map { !it }
                                    )
                                ).bind()
                                dispatch(QuizStore.Message.ChangeCurrentItem(result))
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
                            currentItem = it.currentItem
                        )

                        this is QuizStore.State.Content -> when (val msg = it) {
                            is QuizStore.Message.Init -> this
                            is QuizStore.Message.ChangeCurrentItem -> copy(
                                currentItem = msg.item
                            )

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