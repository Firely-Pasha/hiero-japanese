package space.compoze.hiero.domain.section.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collection.CollectionRepository
import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionGetById(
    private val sectionRepository: SectionRepository
) {

    operator fun invoke(sectionId: String) = either {
        sectionRepository.getById(sectionId).bind()
    }

    operator fun invoke(sectionIds: List<String>) = either {
        sectionRepository.getByIds(sectionIds).bind()
    }

}