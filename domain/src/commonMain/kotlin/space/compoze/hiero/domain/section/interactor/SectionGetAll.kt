package space.compoze.hiero.domain.section.interactor

import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionGetAll(
    private val sectionRepository: SectionRepository
) {

    operator fun invoke() = sectionRepository.getAll()

}