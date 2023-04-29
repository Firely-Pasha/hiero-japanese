package space.compoze.hiero.ui.shared.collection

import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.CollectionModel
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel
import kotlin.random.Random


sealed interface CollectionState {
    object Loading : CollectionState
    data class Content(
        val collection: CollectionModel,
        val items: List<CollectionItemModel>,
    ) : CollectionState

    data class Error(
        val error: DomainError,
    ) : CollectionState
}

sealed interface CollectionIntent {
    object AddItem : CollectionIntent
}

sealed interface CollectionMessage {
    data class AddItem(
        val item: CollectionItemModel,
    ) : CollectionMessage

    data class Error(
        val error: DomainError,
    ) : CollectionMessage

    data class SetCollection(
        val collection: CollectionModel,
    ) : CollectionMessage

    data class InitCollection(
        val collection: CollectionModel,
        val items: List<CollectionItemModel>,
    ) : CollectionMessage
}

sealed interface CollectionLabel {
    data class ShowMessage(
        val message: String,
    ) : CollectionLabel
}

sealed interface CollectionAction {
    data class Loaded(
        val collection: CollectionModel,
        val items: List<CollectionItemModel>,
    ) : CollectionAction

    data class LoadingError(
        val error: DomainError
    ) : CollectionAction
}

interface CollectionStore : Store<CollectionIntent, CollectionState, CollectionLabel>