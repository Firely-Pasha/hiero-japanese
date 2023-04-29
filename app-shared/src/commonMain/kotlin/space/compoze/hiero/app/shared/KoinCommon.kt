package space.compoze.hiero.app.shared

import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.data.collection.CollectionRepository
import space.compoze.hiero.data.collectionitem.CollectionItemRepository
import space.compoze.hiero.domain.collection.interactor.CollectionGetByUuidUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfCompositionUseCase

internal expect fun appPlatformModule(): Module

fun appModule() = appPlatformModule() + module {
    single { Database(get()) }
} + dataModule() + domainModule()

fun dataModule() = module {
    single { CollectionRepository(get()) } bind
            space.compoze.hiero.domain.collection.CollectionRepository::class
    single { CollectionItemRepository(get()) } bind
            space.compoze.hiero.domain.collectionitem.CollectionItemRepository::class
}

fun domainModule() = module {
    single { CollectionGetByUuidUseCase(get()) }
    single { CollectionItemGetOfCompositionUseCase(get()) }
}
