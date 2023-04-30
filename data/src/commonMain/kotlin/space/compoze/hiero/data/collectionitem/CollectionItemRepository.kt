package space.compoze.hiero.data.collectionitem

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.toOption
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.data.collection.toDomainModel
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel

class CollectionItemRepository(
    database: Database,
) : CollectionItemRepository {

    private val collectionItems = database.collectionItemQueries

    override fun getOfCollection(collectionId: String) = either {
        catch({
            collectionItems.getOfCollection(collectionId)
                .executeAsList()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("Db Error", it))
        }
    }

    override fun getOfSection(sectionIds: List<String>) = either {
        catch({
            collectionItems.getOfSections(sectionIds = sectionIds)
                .executeAsList()
                .groupBy({ it.sectionId }, { it.toDomainModel() })
        }) {
            raise(DomainError("Db Error", it))
        }
    }

    override fun getOfSection(sectionId: String) = either {
        getOfSection(listOf(sectionId))
            .bind()[sectionId]
            .orEmpty()
    }


}