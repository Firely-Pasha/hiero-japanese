package space.compoze.hiero.ui.shared.section.state

import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.data.CollectionModel
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.section.model.data.SectionModel

interface SectionStore : Store<SectionStore.Intent, SectionStore.State, Nothing> {


    sealed interface State {
        object Loading : State
        data class Content(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
            val items: List<CollectionItemModel>,
        ) : State

        data class Error(
            val error: DomainError,
        ) : State
    }

    sealed interface Action {
        data class Loaded(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
            val items: List<CollectionItemModel>,
        ) : Action

        data class LoadingError(
            val error: DomainError,
        ) : Action
    }

    sealed interface Intent {
        data class ToggleItemSelect(val itemId: Long) : Intent
        data class ToggleItemBookmark(val itemId: Long) : Intent
        object SelectAll : Intent
        object ClearAll : Intent
    }

    sealed interface Message {
        data class Error(
            val error: DomainError,
        ) : Message

        data class Init(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
            val items: List<CollectionItemModel>,
        ) : Message

        data class SelectItem(
            val itemId: Long,
            val value: Boolean,
        ) : Message

        data class SetItems(
            val items: List<CollectionItemModel>,
        ): Message
    }
}