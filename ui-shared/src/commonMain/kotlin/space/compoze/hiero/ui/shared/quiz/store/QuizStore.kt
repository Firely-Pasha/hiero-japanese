package space.compoze.hiero.ui.shared.quiz.store

import androidx.compose.runtime.Immutable
import arrow.core.Option
import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemVariantModel
import space.compoze.hiero.domain.section.model.data.SectionModel

interface QuizStore : Store<QuizStore.Intent, QuizStore.State, Nothing> {

    sealed interface Action {
        data class Loaded(
            val sections: List<SectionModel>,
            val variants: List<CollectionItemVariantModel>,
            val items: List<CollectionItemModel>,
        ) : Action

        data class LoadingError(
            val error: DomainError,
        ) : Action
    }

    sealed interface State {
        @Immutable
        object Loading: State
        @Immutable
        data class Content(
            val sections: List<SectionModel>,
            val variants: List<CollectionItemVariantModel>,
            val items: List<CollectionItemModel>,
            val itemPool: List<CollectionItemModel>,
            val currentItem: CollectionItemModel,
            val areVariantsSwapped: Boolean,
        ): State
        @Immutable
        object Error: State
    }

    sealed interface Intent {
        object NextItem : Intent
        object BookmarkCurrentItem : Intent
        object SwapVariants : Intent
    }

    sealed interface Message {
        data class Init(
            val sections: List<SectionModel>,
            val variants: List<CollectionItemVariantModel>,
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
        data class SetSwapVariants(
            val isSwapped: Boolean,
        ) : Message
    }

}