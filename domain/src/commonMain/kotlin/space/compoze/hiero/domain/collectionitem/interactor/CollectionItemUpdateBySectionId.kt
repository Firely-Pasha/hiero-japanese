package space.compoze.hiero.domain.collectionitem.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

class CollectionItemUpdateBySectionId(
    private val collectionItemGetOfSectionUseCase: CollectionItemGetOfSectionUseCase,
    private val collectionItemUpdateById: CollectionItemUpdateById,
) {

    operator fun invoke(
        sectionId: String,
        data: CollectionItemMutationData,
    ) = either {
        val items = collectionItemGetOfSectionUseCase(sectionId).bind()
        items.mapNotNull {
            collectionItemUpdateById(it.id, data).getOrNull()
        }
    }

}