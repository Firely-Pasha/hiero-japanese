package space.compoze.hiero.domain.collectionitem.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository

class CollectionItemGetCountOfSection(
    private val collectionItemRepository: CollectionItemRepository
) {

    operator fun invoke(collectionId: String) = either {
        collectionItemRepository.countOfSection(listOf(collectionId)).bind()[collectionId] ?: 0
    }

    operator fun invoke(collectionIds: List<String>) {
        collectionItemRepository.countOfSection(collectionIds)
    }


}