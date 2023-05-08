package space.compoze.hiero.domain.collectionitem.interactor

import space.compoze.hiero.domain.collectionitem.CollectionItemRepository

class CollectionItemGetByIdsUseCase(
    private val collectionItemRepository: CollectionItemRepository,
) {

    operator fun invoke(collectionItemIds: List<Long>) =
        collectionItemRepository.getByIds(collectionItemIds)

}