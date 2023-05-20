package space.compoze.hiero.domain.sectionpreview.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfSectionUseCase
import space.compoze.hiero.domain.sectionpreview.model.data.SectionPreview

class SectionPreviewGet(
    private val collectionItemGetOfSectionUseCase: CollectionItemGetOfSectionUseCase,
) {
    fun single(sectionId: String) = either {
        val sectionItems = collectionItemGetOfSectionUseCase(sectionId).bind()
//        SectionPreview.Generated(
//            totalItems = sectionItems.s
//        ) as SectionPreview
    }
}