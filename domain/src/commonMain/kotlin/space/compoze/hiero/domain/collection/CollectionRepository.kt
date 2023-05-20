package space.compoze.hiero.domain.collection

import arrow.core.Either
import arrow.core.Option
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.data.CollectionModel
import space.compoze.hiero.domain.collection.model.mutation.CollectionMutation

interface CollectionRepository {

    fun getById(uuid: String): Either<DomainError, Option<CollectionModel>>
    fun getAll(): Either<DomainError, List<CollectionModel>>
    fun update(collectionId: String, data: CollectionMutation): Either<DomainError, CollectionModel>
}