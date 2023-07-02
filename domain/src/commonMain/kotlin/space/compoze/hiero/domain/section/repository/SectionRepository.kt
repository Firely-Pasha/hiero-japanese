package space.compoze.hiero.domain.section.repository

import arrow.core.Either
import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.model.mutation.CollectionMutation
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation
import space.compoze.hiero.domain.section.model.mutate.SectionMutation

interface SectionRepository {
    fun getByIds(sectionIds: List<String>): Either<DomainError, Map<String, Option<SectionModel>>>
    fun getByCollection(collectionId: String): Either<DomainError, List<SectionModel>>
    fun getAll(): Either<DomainError, List<SectionModel>>
    fun flowByCollection(collectionId: String): Flow<List<SectionModel>>
    fun update(sectionId: String, data: SectionMutation): Either<DomainError, SectionModel>
    fun updateComputed(sectionId: String, data: SectionComputedMutation): Either<DomainError, SectionModel>
    fun getById(sectionId: String): Either<DomainError, Option<SectionModel>>
}