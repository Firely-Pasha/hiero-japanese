package space.compoze.hiero.ui.shared.quiz.store

import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel

sealed interface QuizAction {
    data class Loaded(
        val items: List<CollectionItemModel>,
    ) : QuizAction

    data class LoadingError(
        val error: DomainError,
    ) : QuizAction
}
