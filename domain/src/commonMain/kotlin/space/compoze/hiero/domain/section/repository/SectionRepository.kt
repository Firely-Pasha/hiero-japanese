package space.compoze.hiero.domain.section.repository

import arrow.core.Either
import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.section.model.SectionModel
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation
import space.compoze.hiero.domain.section.model.mutate.SectionMutationData

interface SectionRepository {
    fun getById(sectionId: String): Either<DomainError, Option<SectionModel>>
    fun getByCollection(collectionId: String): Either<DomainError, List<SectionModel>>
    fun flowByCollection(collectionId: String): Flow<List<SectionModel>>
    fun updateComputed(sectionId: String, data: SectionComputedMutation): Either<DomainError, SectionModel>
}