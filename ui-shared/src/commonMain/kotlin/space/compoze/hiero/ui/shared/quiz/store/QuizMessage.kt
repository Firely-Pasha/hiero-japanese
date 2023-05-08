package space.compoze.hiero.ui.shared.quiz.store

import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel

sealed interface QuizMessage {
    data class Init(
        val items: List<CollectionItemModel>,
        val currentItem: CollectionItemModel,
    ) : QuizMessage
    data class ChangeCurrentItem(
        val item: CollectionItemModel,
    ) : QuizMessage
}
