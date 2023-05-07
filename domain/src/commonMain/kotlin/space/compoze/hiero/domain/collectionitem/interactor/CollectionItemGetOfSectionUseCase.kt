package space.compoze.hiero.domain.collectionitem.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository

class CollectionItemGetOfSectionUseCase(
    private val collectionItemRepository: CollectionItemRepository
) {

    operator fun invoke(sectionIds: List<String>) = either {
        collectionItemRepository.getOfSection(sectionIds).bind()
    }

    operator fun invoke(sectionId: String) = either {
        invoke(listOf(sectionId)).bind()[sectionId].orEmpty()
    }

}