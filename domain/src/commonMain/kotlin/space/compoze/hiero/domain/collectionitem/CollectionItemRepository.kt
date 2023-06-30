package space.compoze.hiero.domain.collectionitem

import arrow.core.Either
import arrow.core.Option
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

interface CollectionItemRepository {
    fun getById(collectionItemId: String): Either<DomainError, Option<CollectionItemModel>>
    fun getByIds(collectionItemIds: List<String>): Either<DomainError, List<CollectionItemModel>>
    fun getOfCollection(collectionId: String): Either<DomainError, List<CollectionItemModel>>
    fun getOfSection(sectionIds: List<String>): Either<DomainError, Map<String, List<CollectionItemModel>>>
    fun update(
        collectionItemId: String,
        data: CollectionItemMutationData,
    ): Either<DomainError, CollectionItemModel>
    fun countOfCollection(collectionIds: List<String>): Either<DomainError, Map<String, Long>>
    fun countOfSection(sectionIds: List<String>): Either<DomainError, Map<String, Long>>
}