package space.compoze.hiero.domain.collectionitem.interactor.notification

import arrow.core.raise.either
import space.compoze.hiero.domain.collectionitem.CollectionItemNotifier

class CollectionItemNotificationGetFlowUseCase(
    private val collectionItemNotifier: CollectionItemNotifier
) {

    operator fun invoke() = collectionItemNotifier.listen()

}