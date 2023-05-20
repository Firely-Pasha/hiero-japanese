package space.compoze.hiero.domain.collectionitem.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository

class CollectionItemGetCountOfCollection(
    private val collectionItemRepository: CollectionItemRepository,
) {

    operator fun invoke(sectionId: String) = either {
        collectionItemRepository.countOfSection(listOf(sectionId)).bind()[sectionId] ?: 0
    }

    operator fun invoke(sectionId: List<String>) {
        collectionItemRepository.countOfSection(sectionId)
    }

}