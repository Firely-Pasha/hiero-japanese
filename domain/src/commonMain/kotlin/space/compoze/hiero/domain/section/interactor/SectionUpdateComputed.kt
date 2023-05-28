package space.compoze.hiero.domain.section.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation
import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionUpdateComputed(
    private val sectionRepository: SectionRepository
) {

    operator fun invoke(sectionId: String, mutation: SectionComputedMutation) = either {
        sectionRepository.updateComputed(sectionId, mutation).bind()
    }

}
