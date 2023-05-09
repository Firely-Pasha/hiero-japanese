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
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetByIdsUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateByIdUseCase
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

class QuizStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionItemGetByIdsUseCase: CollectionItemGetByIdsUseCase by inject()
    private val collectionItemUpdateByIdUseCase: CollectionItemUpdateByIdUseCase by inject()

    fun create(items: List<Long>): QuizStore =
        object : QuizStore,
            Store<QuizIntent, QuizState, Nothing> by storeFactory.create<QuizIntent, QuizAction, QuizMessage, QuizState, Nothing>(
                name = "quiz_store",
                initialState = QuizState.Loading,
                bootstrapper = coroutineBootstrapper {
                    either {
                        val result = collectionItemGetByIdsUseCase(items).bind()
                        dispatch(
                            QuizAction.Loaded(
                                items = result,
                            )
                        )
                    }.onLeft {
                        dispatch(QuizAction.LoadingError(it))
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<QuizAction.Loaded> {
                        dispatch(
                            QuizMessage.Init(
                                items = it.items,
                                currentItem = it.items.random()
                            )
                        )
                    }
                    onIntent<QuizIntent.NextItem> {
                        state.withState<QuizState.Content> {
                            dispatch(QuizMessage.ChangeCurrentItem(item = run {
                                if (this.items.all { it == currentItem }) {
                                    return@run currentItem
                                }
                                var randomItem = this.items.random()
                                while (randomItem == currentItem) {
                                    randomItem = this.items.random()
                                }
                                randomItem
                            }))
                        }
                    }
                    onIntent<QuizIntent.BookmarkCurrentItem> {
                        state.withState<QuizState.Content> {
                            either {
                                val result = collectionItemUpdateByIdUseCase(currentItem.id,
                                    CollectionItemMutationData(
                                        isBookmarked = currentItem.isBookmarked.some().map { !it }
                                    )).bind()
                                dispatch(QuizMessage.ChangeCurrentItem(result))
                                dispatch(QuizMessage.SetItem(result))
                            }.onLeft {
                                println(it)
                            }
                        }
                    }
                },
                reducer = {
                    when (val msg = it) {
                        is QuizMessage.Init -> QuizState.Content(
                            items = msg.items,
                            currentItem = msg.currentItem
                        )

                        is QuizMessage.ChangeCurrentItem -> applyState<QuizState.Content> {
                            copy(
                                currentItem = msg.item
                            )
                        }

                        is QuizMessage.SetItem -> applyState<QuizState.Content> {
                            copy(
                                items = this.items.toMutableList().apply {
                                    val index = indexOfFirst { it.id == msg.item.id }
                                    set(index, msg.item)
                                }.toList()
                            )
                        }
                    }
                }
            ) {}

}