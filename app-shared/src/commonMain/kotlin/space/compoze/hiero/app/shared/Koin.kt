package space.compoze.hiero.app.shared

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.data.collection.CollectionRepository
import space.compoze.hiero.data.collectionitem.CollectionItemRepository
import space.compoze.hiero.data.section.SectionRepository
import space.compoze.hiero.data.settings.SettingsRepository
import space.compoze.hiero.domain.application.interactor.ApplicationInit
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.domain.collection.interactor.CollectionGetAll
import space.compoze.hiero.domain.collection.interactor.CollectionGetById
import space.compoze.hiero.domain.collection.interactor.CollectionUpdate
import space.compoze.hiero.domain.collectionitem.CollectionItemNotifier
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetByIds
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetCountOfCollection
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetCountOfSection
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfSection
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateById
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateBySectionId
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationGetFlow
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationListen
import space.compoze.hiero.domain.section.interactor.SectionGetAll
import space.compoze.hiero.domain.section.interactor.SectionGetById
import space.compoze.hiero.domain.section.interactor.SectionGetOfCollection
import space.compoze.hiero.domain.section.interactor.SectionUpdate
import space.compoze.hiero.domain.section.interactor.SectionUpdateComputed
import space.compoze.hiero.domain.settings.interactor.SettingsGetTheme
import space.compoze.hiero.domain.settings.interactor.SettingsSetTheme

internal expect fun appPlatformModule(): Module

fun appModule() = appPlatformModule() + module {
    single { PlatformDispatchers } bind AppDispatchers::class
    singleOf(Database::invoke)
} + dataModule() + domainModule()

fun dataModule() = module {
    singleOf(::CollectionRepository) bind
            space.compoze.hiero.domain.collection.CollectionRepository::class
    singleOf(::SectionRepository) bind
            space.compoze.hiero.domain.section.repository.SectionRepository::class
    singleOf(::CollectionItemRepository) bind
            space.compoze.hiero.domain.collectionitem.CollectionItemRepository::class
    singleOf(::SettingsRepository) bind
            space.compoze.hiero.domain.settings.repository.SettingsRepository::class
}

fun domainModule() = module {

    singleOf(::ApplicationInit)

    singleOf(::CollectionGetAll)
    singleOf(::CollectionGetById)
    singleOf(::CollectionUpdate)

    singleOf(::CollectionItemGetByIds)
    singleOf(::CollectionItemGetCountOfCollection)
    singleOf(::CollectionItemGetCountOfSection)
    singleOf(::CollectionItemGetOfSection)
    singleOf(::CollectionItemUpdateById)
    singleOf(::CollectionItemUpdateBySectionId)
    singleOf(::CollectionItemNotifier)
    singleOf(::CollectionItemNotificationGetFlow)
    singleOf(::CollectionItemNotificationListen)

    singleOf(::SectionGetAll)
    singleOf(::SectionGetById)
    singleOf(::SectionGetOfCollection)
    singleOf(::SectionUpdate)
    singleOf(::SectionUpdateComputed)

    singleOf(::SettingsGetTheme)
    singleOf(::SettingsSetTheme)

}
