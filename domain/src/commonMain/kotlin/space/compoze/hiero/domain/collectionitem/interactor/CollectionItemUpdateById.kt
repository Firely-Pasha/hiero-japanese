package space.compoze.hiero.domain.collectionitem.interactor

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.CollectionItemNotification
import space.compoze.hiero.domain.collectionitem.CollectionItemNotifier
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

class CollectionItemUpdateById(
    private val collectionItemGetByIds: CollectionItemGetByIds,
    private val collectionItemRepository: CollectionItemRepository,
    private val collectionItemNotifier: CollectionItemNotifier,
) {

    operator fun invoke(
        collectionItemId: String,
        data: CollectionItemMutationData,
    ) = either {
        val item = collectionItemGetByIds(listOf(collectionItemId)).bind().first()
        invoke(item, data).bind()
    }

    operator fun invoke(
        collectionItem: CollectionItemModel,
        data: CollectionItemMutationData,
    ) = either {
        collectionItemRepository.update(collectionItem.id, data).bind().also {
            collectionItemNotifier.notify(
                CollectionItemNotification.Changed(collectionItem, it)
            )
        }
    }

}