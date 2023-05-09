package space.compoze.hiero.domain.collectionitem

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel


sealed interface CollectionItemNotification {
    data class Changed(
        val prev: CollectionItemModel,
        val new: CollectionItemModel,
    ) : CollectionItemNotification
}

class CollectionItemNotifier {

    private val notifications = MutableSharedFlow<CollectionItemNotification>()

    internal fun listen() = notifications.asSharedFlow()

    internal fun notify(notification: CollectionItemNotification) {
        GlobalScope.launch {
            notifications.emit(notification)
        }
    }
}