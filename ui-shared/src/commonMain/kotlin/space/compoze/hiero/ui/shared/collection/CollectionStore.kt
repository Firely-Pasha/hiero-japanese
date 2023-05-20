package space.compoze.hiero.ui.shared.collection

import arrow.core.Option
import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.data.CollectionModel
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.domain.sectionpreview.model.data.SectionPreview


sealed interface CollectionState {
    object Loading : CollectionState
    data class Content(
        val collection: CollectionModel,
        val sections: List<SectionModel>,
        val previews: List<Option<SectionPreview>>,
    ) : CollectionState

    data class Error(
        val error: DomainError,
    ) : CollectionState
}

sealed interface CollectionAction {
    data class Loaded(
        val collection: CollectionModel,
        val sections: List<SectionModel>,
        val previews: List<Option<SectionPreview>>,
    ) : CollectionAction

    data class LoadingError(
        val error: DomainError,
    ) : CollectionAction
}

sealed interface CollectionIntent {
    object AddSection : CollectionIntent
}

sealed interface CollectionMessage {
    data class AddSection(
        val section: SectionModel,
    ) : CollectionMessage

    data class InitCollection(
        val collection: CollectionModel,
        val sections: List<SectionModel>,
        val previews: List<Option<SectionPreview>>,
    ) : CollectionMessage

    data class Error(
        val error: DomainError,
    ) : CollectionMessage

    data class SetCollection(
        val collection: CollectionModel,
    ) : CollectionMessage

    data class SetSections(
        val sections: List<SectionModel>,
        val previews: List<Option<SectionPreview>>
    ) : CollectionMessage
}

sealed interface CollectionLabel {
    data class ShowMessage(
        val message: String,
    ) : CollectionLabel
}

interface CollectionStore : Store<CollectionIntent, CollectionState, CollectionLabel>