package space.compoze.hiero.domain.collectionitem.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

class CollectionItemUpdateBySectionId(
    private val collectionItemGetOfSection: CollectionItemGetOfSection,
    private val collectionItemUpdateById: CollectionItemUpdateById,
) {

    operator fun invoke(
        sectionId: String,
        data: CollectionItemMutationData,
    ) = either {
        val items = collectionItemGetOfSection(sectionId).bind()
        items.mapNotNull {
            if (!it.isBookmarked && it.type != "empty") {
                collectionItemUpdateById(it, data).getOrNull()
            } else {
                it
            }
        }
    }

}
