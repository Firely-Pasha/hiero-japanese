package space.compoze.hiero.domain.collectionitem

import arrow.core.Either
import arrow.core.Option
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

interface CollectionItemRepository {
    fun getById(collectionItemId: Long): Either<DomainError, Option<CollectionItemModel>>
    fun getByIds(collectionItemIds: List<Long>): Either<DomainError, List<CollectionItemModel>>
    fun getOfCollection(collectionId: String): Either<DomainError, List<CollectionItemModel>>
    fun getOfSection(sectionIds: List<String>): Either<DomainError, Map<String, List<CollectionItemModel>>>
    fun update(
        collectionItemId: Long,
        data: CollectionItemMutationData,
    ): Either<DomainError, CollectionItemModel>
}