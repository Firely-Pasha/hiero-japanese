package space.compoze.hiero.domain.collection.interactor

import space.compoze.hiero.domain.collection.CollectionRepository

class CollectionGetAll(
    private val collectionRepository: CollectionRepository
) {

    operator fun invoke() = collectionRepository.getAll()

}