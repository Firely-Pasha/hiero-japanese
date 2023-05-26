package space.compoze.hiero.ui.shared.collection.store

import arrow.core.Option
import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.data.CollectionModel
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.domain.sectionpreview.model.data.SectionPreview



interface CollectionStore : Store<CollectionStore.Intent, CollectionStore.State, CollectionStore.Label> {

    sealed interface State {
        object Loading : State
        data class Content(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
            val previews: List<Option<SectionPreview>>,
        ) : State

        data class Error(
            val error: DomainError,
        ) : State
    }

    sealed interface Action {
        data class Loaded(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
            val previews: List<Option<SectionPreview>>,
        ) : Action

        data class LoadingError(
            val error: DomainError,
        ) : Action
    }

    sealed interface Intent {
        object AddSection : Intent
    }

    sealed interface Message {

        data class InitCollection(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
            val previews: List<Option<SectionPreview>>,
        ) : Message

        data class Error(
            val error: DomainError,
        ) : Message

        data class SetCollection(
            val collection: CollectionModel,
        ) : Message

        data class SetSections(
            val sections: List<SectionModel>,
            val previews: List<Option<SectionPreview>>
        ) : Message
    }

    sealed interface Label {
        data class ShowMessage(
            val message: String,
        ) : Label
    }

}