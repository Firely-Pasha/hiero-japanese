package space.compoze.hiero.domain.sectionpreview.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfSection

class SectionPreviewGet(
    private val collectionItemGetOfSection: CollectionItemGetOfSection,
) {
    fun single(sectionId: String) = either {
        val sectionItems = collectionItemGetOfSection(sectionId).bind()
//        SectionPreview.Generated(
//            totalItems = sectionItems.s
//        ) as SectionPreview
    }
}