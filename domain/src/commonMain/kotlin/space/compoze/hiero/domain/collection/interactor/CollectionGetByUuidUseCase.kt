package space.compoze.hiero.domain.collection.interactor

import arrow.core.raise.Raise
import arrow.core.raise.either
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.CollectionRepository

class CollectionGetByUuidUseCase(
    private val collectionRepository: CollectionRepository
) {

    operator fun invoke(uuid: String) = either {
        collectionRepository.getByUuid(uuid).bind()
    }

}