package space.compoze.hiero.domain.section.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionGetOfCollection(
    private val sectionRepository: SectionRepository,
) {

    operator fun invoke(collectionId: String) = either {
        sectionRepository.getByCollection(collectionId).bind()
    }

    fun flow(collectionId: String) =
        sectionRepository.flowByCollection(collectionId)

}