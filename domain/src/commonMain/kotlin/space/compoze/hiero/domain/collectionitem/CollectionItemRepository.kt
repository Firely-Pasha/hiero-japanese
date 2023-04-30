package space.compoze.hiero.domain.collectionitem

import arrow.core.Either
import arrow.core.Option
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel

interface CollectionItemRepository {

    fun getOfCollection(collectionId: String): Either<DomainError, List<CollectionItemModel>>
    fun getOfSection(sectionId: String): Either<DomainError, List<CollectionItemModel>>

}