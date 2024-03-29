package space.compoze.hiero.ui.shared.section.state

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.data.CollectionModel
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemVariantModel
import space.compoze.hiero.domain.section.model.data.SectionModel

interface SectionStore : Store<SectionStore.Intent, SectionStore.State, Nothing> {


    sealed interface State {
        @Immutable
        object Loading : State
        @Immutable
        data class Content(
            val collection: CollectionModel,
            val section: SectionModel,
            val primaryVariant: CollectionItemVariantModel,
            val secondaryVariant: CollectionItemVariantModel,
            val items: List<CollectionItemModel>,
            val selectionMode: Boolean,
        ) : State

        @Immutable
        data class Error(
            val error: DomainError,
        ) : State
    }

    sealed interface Action {
        data class Loaded(
            val collection: CollectionModel,
            val section: SectionModel,
            val variants: List<CollectionItemVariantModel>,
            val items: List<CollectionItemModel>,
        ) : Action

        data class LoadingError(
            val error: DomainError,
        ) : Action
    }

    sealed interface Intent {
        data class ToggleItemSelect(
            val itemId: String,
        ) : Intent

        data class ToggleItemAndSetSelectMode(
            val itemId: String
        ) : Intent

        data class ToggleItemBySelect(
            val itemId: String
        ) : Intent

        data class ToggleItemBookmark(
            val itemId: String,
        ) : Intent


        object SelectAll : Intent
        object ClearAll : Intent
    }

    sealed interface Message {
        data class Error(
            val error: DomainError,
        ) : Message

        data class Init(
            val collection: CollectionModel,
            val section: SectionModel,
            val primaryVariant: CollectionItemVariantModel,
            val secondaryVariant: CollectionItemVariantModel,
            val items: List<CollectionItemModel>,
        ) : Message

        data class SelectItem(
            val itemId: String,
            val value: Boolean,
        ) : Message

        data class SetItems(
            val items: List<CollectionItemModel>,
        ) : Message

        data class SetSelectionMode(
            val selectionMode: Boolean,
        ) : Message
    }
}