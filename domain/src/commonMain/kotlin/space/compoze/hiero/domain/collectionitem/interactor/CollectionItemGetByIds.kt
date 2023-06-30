package space.compoze.hiero.domain.collectionitem.interactor

import space.compoze.hiero.domain.collectionitem.CollectionItemRepository

class CollectionItemGetByIds(
    private val collectionItemRepository: CollectionItemRepository,
) {

    operator fun invoke(collectionItemIds: List<String>) =
        collectionItemRepository.getByIds(collectionItemIds)

}