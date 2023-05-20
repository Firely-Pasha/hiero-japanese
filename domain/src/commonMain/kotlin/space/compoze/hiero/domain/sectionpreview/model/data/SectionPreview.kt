package space.compoze.hiero.domain.sectionpreview.model.data

import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel

sealed interface SectionPreview {
    data class Generated(
        val totalItems: Int,
    ) : SectionPreview
}