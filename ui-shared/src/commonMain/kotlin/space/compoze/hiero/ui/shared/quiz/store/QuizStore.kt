package space.compoze.hiero.ui.shared.quiz.store

import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel

interface QuizStore : Store<QuizStore.Intent, QuizStore.State, Nothing> {

    sealed interface Action {
        data class Loaded(
            val items: List<CollectionItemModel>,
        ) : Action

        data class LoadingError(
            val error: DomainError,
        ) : Action
    }

    sealed interface State {
        object Loading: State
        data class Content(
            val items: List<CollectionItemModel>,
            val itemPool: List<CollectionItemModel>,
            val currentItem: CollectionItemModel,
        ): State
        object Error: State
    }

    sealed interface Intent {
        object NextItem : Intent
        object BookmarkCurrentItem : Intent
    }

    sealed interface Message {
        data class Init(
            val items: List<CollectionItemModel>,
            val itemPull: List<CollectionItemModel>,
            val currentItem: CollectionItemModel,
        ) : Message
        data class SetItemPull(
            val itemPull: List<CollectionItemModel>,
        ) : Message
        data class SetCurrentItem(
            val item: CollectionItemModel,
        ) : Message
        data class SetItem(
            val item: CollectionItemModel,
        ) : Message
    }

}