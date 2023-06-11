package space.compoze.hiero.ui.shared.collection.store

import androidx.compose.runtime.Immutable
import arrow.core.Option
import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.data.CollectionModel
import space.compoze.hiero.domain.section.model.data.SectionModel



interface CollectionStore : Store<CollectionStore.Intent, CollectionStore.State, CollectionStore.Label> {

    sealed interface State {
        @Immutable
        object Loading : State

        @Immutable
        data class Content(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
            val canStartBookmarkedQuiz: Boolean,
            val canStartQuiz: Boolean,
        ) : State

        @Immutable
        data class Error(
            val error: DomainError,
        ) : State
    }

    sealed interface Action {
        data class Loaded(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
        ) : Action

        data class LoadingError(
            val error: DomainError,
        ) : Action
    }

    sealed interface Intent {
        data class StartQuiz(
            val isBookmarkOnly: Boolean
        ) : Intent
    }

    sealed interface Message {

        data class InitCollection(
            val collection: CollectionModel,
            val sections: List<SectionModel>,
        ) : Message

        data class Error(
            val error: DomainError,
        ) : Message

        data class SetCollection(
            val collection: CollectionModel,
        ) : Message

        data class SetSections(
            val sections: List<SectionModel>,
        ) : Message
    }

    sealed interface Label {
        data class StartQuiz(
            val items: List<Long>,
        ) : Label
    }

}