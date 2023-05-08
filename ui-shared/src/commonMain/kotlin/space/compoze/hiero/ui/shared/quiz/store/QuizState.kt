package space.compoze.hiero.ui.shared.quiz.store

import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel

sealed interface QuizState {
    object Loading: QuizState
    data class Content(
        val items: List<CollectionItemModel>,
        val currentItem: CollectionItemModel,
    ): QuizState
    object Error: QuizState
}