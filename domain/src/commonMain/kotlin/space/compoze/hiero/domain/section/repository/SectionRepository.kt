package space.compoze.hiero.domain.section.repository

import arrow.core.Either
import arrow.core.Option
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.section.model.SectionModel

interface SectionRepository {
    fun getById(sectionId: String): Either<DomainError, Option<SectionModel>>
    fun getByCollection(collectionId: String): Either<DomainError, List<SectionModel>>
}