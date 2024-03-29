package space.compoze.hiero.domain.application.interactor

import arrow.core.raise.either
import kotlinx.coroutines.Job
import space.compoze.hiero.domain.collection.interactor.CollectionGetAll
import space.compoze.hiero.domain.collection.interactor.CollectionUpdate
import space.compoze.hiero.domain.collection.model.mutation.CollectionMutation
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetCountOfCollection
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationListen
import space.compoze.hiero.domain.section.interactor.SectionGetAll
import space.compoze.hiero.domain.section.interactor.SectionUpdate
import space.compoze.hiero.domain.section.model.mutate.SectionMutation

class ApplicationInit(
    private val collectionItemNotificationListen: CollectionItemNotificationListen,
    private val collectionGetAll: CollectionGetAll,
    private val collectionItemGetCountOfCollection: CollectionItemGetCountOfCollection,
    private val collectionUpdate: CollectionUpdate,
    private val sectionGetAll: SectionGetAll,
    private val collectionItemGetCountOfSection: CollectionItemGetCountOfCollection,
    private val sectionUpdate: SectionUpdate,
) {

    private val appJobs = mutableListOf<Job>()

    operator fun invoke() = either {
        appJobs.add(collectionItemNotificationListen())
        initCollections().bind()
    }

    private fun initCollections() = either {
        val collections = collectionGetAll().bind()
        collections.forEach {
            val collectionItemCount = collectionItemGetCountOfCollection(it.id).bind()
            collectionUpdate(it.id, CollectionMutation.UpdateItemsCount(collectionItemCount)).bind()
        }
        val sections = sectionGetAll().bind()
        sections.forEach {
            val collectionItemCount = collectionItemGetCountOfSection(it.id).bind()
            sectionUpdate(it.id, SectionMutation.UpdateItemsCount(collectionItemCount)).bind()
        }
    }

}