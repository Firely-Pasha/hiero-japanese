package space.compoze.hiero.domain.section.interactor

import space.compoze.hiero.domain.collection.CollectionRepository
import space.compoze.hiero.domain.section.model.mutate.SectionMutation
import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionUpdate(
    private val sectionRepository: SectionRepository,
) {

    operator fun invoke(sectionId: String, data: SectionMutation) =
        sectionRepository.update(sectionId, data)

}