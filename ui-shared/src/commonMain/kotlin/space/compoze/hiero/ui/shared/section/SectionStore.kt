package space.compoze.hiero.ui.shared.section

import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.CollectionModel
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel
import space.compoze.hiero.domain.section.model.SectionModel


sealed interface SectionState {
    object Loading : SectionState
    data class Content(
        val collection: CollectionModel,
        val sections: List<SectionModel>,
        val items: Map<String, List<CollectionItemModel>>,
    ) : SectionState

    data class Error(
        val error: DomainError,
    ) : SectionState
}

sealed interface SectionIntent {
    object AddSection : SectionIntent
}

sealed interface SectionMessage {
    data class Error(
        val error: DomainError,
    ) : SectionMessage

    data class InitSection(
        val collection: CollectionModel,
        val section: List<SectionModel>,
        val items: Map<String, List<CollectionItemModel>>,
    ) : SectionMessage
}
sealed interface SectionAction {
    data class Loaded(
        val collection: CollectionModel,
        val sections: List<SectionModel>,
        val items: Map<String, List<CollectionItemModel>>,
    ) : SectionAction

    data class LoadingError(
        val error: DomainError
    ) : SectionAction
}

interface SectionStore : Store<SectionIntent, SectionState, Nothing>