package space.compoze.hiero.app.shared

import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.data.collection.CollectionRepository
import space.compoze.hiero.data.collectionitem.CollectionItemRepository
import space.compoze.hiero.data.section.SectionRepository
import space.compoze.hiero.domain.application.interactor.ApplicationInitUseCase
import space.compoze.hiero.domain.collection.interactor.CollectionGetAll
import space.compoze.hiero.domain.collection.interactor.CollectionGetById
import space.compoze.hiero.domain.collection.interactor.CollectionUpdate
import space.compoze.hiero.domain.collectionitem.CollectionItemNotifier
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetByIdsUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetCountOfCollection
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetCountOfSection
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfSectionUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateByIdUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateBySectionId
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationGetFlowUseCase
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationListenUseCase
import space.compoze.hiero.domain.section.interactor.SectionGetAll
import space.compoze.hiero.domain.section.interactor.SectionGetByIdUseCase
import space.compoze.hiero.domain.section.interactor.SectionGetOfCollection
import space.compoze.hiero.domain.section.interactor.SectionUpdate
import space.compoze.hiero.domain.section.interactor.SectionUpdateComputedUseCase
import space.compoze.hiero.domain.sectionpreview.interactor.SectionPreviewGet

internal expect fun appPlatformModule(): Module

fun appModule() = appPlatformModule() + module {
    single { Database(get()) }
} + dataModule() + domainModule()

fun dataModule() = module {
    single { CollectionRepository(get()) } bind
            space.compoze.hiero.domain.collection.CollectionRepository::class
    single { SectionRepository(get()) } bind
            space.compoze.hiero.domain.section.repository.SectionRepository::class
    single { CollectionItemRepository(get()) } bind
            space.compoze.hiero.domain.collectionitem.CollectionItemRepository::class
}

fun domainModule() = module {

    single { ApplicationInitUseCase(get(), get(), get(), get(), get(), get(), get()) }

    single { CollectionGetAll(get()) }
    single { CollectionGetById(get()) }
    single { CollectionUpdate(get()) }

    single { CollectionItemGetByIdsUseCase(get()) }
    single { CollectionItemGetCountOfCollection(get()) }
    single { CollectionItemGetCountOfSection(get()) }
    single { CollectionItemGetOfSectionUseCase(get()) }
    single { CollectionItemUpdateByIdUseCase(get(), get(), get()) }
    single { CollectionItemUpdateBySectionId(get(), get()) }
    single { CollectionItemNotifier() }
    single { CollectionItemNotificationGetFlowUseCase(get()) }
    single { CollectionItemNotificationListenUseCase(get(), get()) }

    single { SectionGetAll(get()) }
    single { SectionGetByIdUseCase(get()) }
    single { SectionGetOfCollection(get()) }
    single { SectionUpdate(get()) }
    single { SectionUpdateComputedUseCase(get()) }

    single { SectionPreviewGet(get()) }

}