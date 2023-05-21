package space.compoze.hiero.domain.collectionitem.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository

class CollectionItemGetOfComposition(
    private val collectionItemRepository: CollectionItemRepository
) {

    operator fun invoke(collectionId: String) = either {
        collectionItemRepository.getOfCollection(collectionId).bind()
    }

}