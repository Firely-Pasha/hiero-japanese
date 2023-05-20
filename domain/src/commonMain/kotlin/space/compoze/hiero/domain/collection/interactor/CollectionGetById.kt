package space.compoze.hiero.domain.collection.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collection.CollectionRepository

class CollectionGetById(
    private val collectionRepository: CollectionRepository
) {

    operator fun invoke(uuid: String) = either {
        collectionRepository.getById(uuid).bind()
    }

}