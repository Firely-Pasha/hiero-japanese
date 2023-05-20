package space.compoze.hiero.domain.collection.interactor

import space.compoze.hiero.domain.collection.CollectionRepository
import space.compoze.hiero.domain.collection.model.mutation.CollectionMutation

class CollectionUpdate(
    private val collectionRepository: CollectionRepository,
) {

    operator fun invoke(collectionId: String, data: CollectionMutation) =
        collectionRepository.update(collectionId, data)

}