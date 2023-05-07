package space.compoze.hiero.domain.collectionitem.interactor

import space.compoze.hiero.domain.collectionitem.CollectionItemRepository
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

class CollectionItemUpdateById(
    private val collectionItemRepository: CollectionItemRepository,
) {

    operator fun invoke(
        collectionItemId: Long,
        data: CollectionItemMutationData,
    ) = collectionItemRepository.update(collectionItemId, data)

}