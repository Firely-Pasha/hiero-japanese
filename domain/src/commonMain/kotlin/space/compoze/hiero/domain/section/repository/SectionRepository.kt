package space.compoze.hiero.domain.section.repository

import arrow.core.Either
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.section.model.SectionModel

interface SectionRepository {
    fun getByCollection(collectionId: String): Either<DomainError, List<SectionModel>>
}