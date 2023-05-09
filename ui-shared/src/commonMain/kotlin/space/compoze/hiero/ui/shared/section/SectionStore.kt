package space.compoze.hiero.ui.shared.section

import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.CollectionModel
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.section.model.SectionModel


sealed interface SectionState {
    object Loading : SectionState
    data class Content(
        val collection: CollectionModel,
        val sections: List<SectionModel>,
        val items: List<CollectionItemModel>,
    ) : SectionState

    data class Error(
        val error: DomainError,
    ) : SectionState
}

sealed interface SectionIntent {
    data class ToggleItemSelect(val itemId: Long) : SectionIntent
    data class ToggleItemBookmark(val itemId: Long) : SectionIntent
    object SelectAll : SectionIntent
    object ClearAll : SectionIntent
}

sealed interface SectionMessage {
    data class Error(
        val error: DomainError,
    ) : SectionMessage

    data class InitSection(
        val collection: CollectionModel,
        val sections: List<SectionModel>,
        val items: List<CollectionItemModel>,
    ) : SectionMessage

    data class SelectItem(
        val itemId: Long,
        val value: Boolean,
    ) : SectionMessage

    data class SetItems(
        val items: List<CollectionItemModel>,
    ): SectionMessage
}

sealed interface SectionAction {
    data class Loaded(
        val collection: CollectionModel,
        val sections: List<SectionModel>,
        val items: List<CollectionItemModel>,
    ) : SectionAction

    data class LoadingError(
        val error: DomainError,
    ) : SectionAction
}

interface SectionStore : Store<SectionIntent, SectionState, Nothing>