package space.compoze.hiero.domain.section.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collection.CollectionRepository
import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionGetByIdUseCase(
    private val sectionRepository: SectionRepository
) {

    operator fun invoke(collectionId: String) = either {
        sectionRepository.getById(collectionId).bind()
    }

}