package space.compoze.hiero.app.shared

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
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
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateByIdUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemUpdateBySectionId
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationGetFlowUseCase
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationListenUseCase
import space.compoze.hiero.domain.section.interactor.SectionGetAll
import space.compoze.hiero.domain.section.interactor.SectionGetByIdUseCase
import space.compoze.hiero.domain.section.interactor.SectionGetOfCollection
import space.compoze.hiero.domain.section.interactor.SectionUpdate
import space.compoze.hiero.domain.section.interactor.SectionUpdateComputed
import space.compoze.hiero.domain.settings.interactor.SettingsGetTheme
import space.compoze.hiero.domain.settings.interactor.SettingsSetTheme

internal expect fun appPlatformModule(): Module

fun appModule() = appPlatformModule() + module {
    single { PlatformDispatchers } bind AppDispatchers::class
    single { Database(get()) }
} + dataModule() + domainModule()

fun dataModule() = module {
    single { CollectionRepository(get()) } bind
            space.compoze.hiero.domain.collection.CollectionRepository::class
    single { SectionRepository(get()) } bind
            space.compoze.hiero.domain.section.repository.SectionRepository::class
    single { CollectionItemRepository(get()) } bind
            space.compoze.hiero.domain.collectionitem.CollectionItemRepository::class
    single { SettingsRepository(get(), get()) } bind
            space.compoze.hiero.domain.settings.repository.SettingsRepository::class
}

fun domainModule() = module {

    single { ApplicationInit(get(), get(), get(), get(), get(), get(), get()) }

    single { CollectionGetAll(get()) }
    single { CollectionGetById(get()) }
    single { CollectionUpdate(get()) }

    single { CollectionItemGetByIds(get()) }
    single { CollectionItemGetCountOfCollection(get()) }
    single { CollectionItemGetCountOfSection(get()) }
    single { CollectionItemGetOfSection(get()) }
    single { CollectionItemUpdateByIdUseCase(get(), get(), get()) }
    single { CollectionItemUpdateBySectionId(get(), get()) }
    single { CollectionItemNotifier() }
    single { CollectionItemNotificationGetFlowUseCase(get()) }
    single { CollectionItemNotificationListenUseCase(get(), get()) }

    single { SectionGetAll(get()) }
    single { SectionGetByIdUseCase(get()) }
    single { SectionGetOfCollection(get()) }
    single { SectionUpdate(get()) }
    single { SectionUpdateComputed(get()) }

    single { SettingsGetTheme(get()) }
    single { SettingsSetTheme(get()) }

}
