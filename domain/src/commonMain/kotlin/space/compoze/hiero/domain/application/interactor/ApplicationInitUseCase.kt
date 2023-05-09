package space.compoze.hiero.domain.application.interactor

import kotlinx.coroutines.Job
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationListenUseCase

class ApplicationInitUseCase(
    val collectionItemNotificationListenUseCase: CollectionItemNotificationListenUseCase,
) {

    private val appJobs = mutableListOf<Job>()

    operator fun invoke() {
        appJobs.add(collectionItemNotificationListenUseCase())
    }

}