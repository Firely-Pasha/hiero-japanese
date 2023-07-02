package space.compoze.hiero.domain.variant

import arrow.core.raise.either
import space.compoze.hiero.domain.collection.model.data.CollectionModel
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository

class VariantGetOfCollection(
    private val collectionItemRepository: CollectionItemRepository
) {

    operator fun invoke(collection: CollectionModel) = either {
        invoke(listOf(collection.id)).bind()[collection.id].orEmpty()
    }

    operator fun invoke(collectionIds: List<String>) =
        collectionItemRepository.getVariantsOfCollection(collectionIds)

}