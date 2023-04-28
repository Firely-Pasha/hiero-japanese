package space.compoze.hiero.domain.collection

import arrow.core.Either
import arrow.core.Option
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.CollectionModel

interface CollectionRepository {

    fun getByUuid(uuid: String): Either<DomainError, Option<CollectionModel>>

}