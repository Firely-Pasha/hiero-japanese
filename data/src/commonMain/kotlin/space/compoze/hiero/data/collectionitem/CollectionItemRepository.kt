package space.compoze.hiero.data.collectionitem

import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.toOption
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

class CollectionItemRepository(
    database: Database,
) : CollectionItemRepository {

    private val collectionItems = database.collectionItemQueries

    override fun getById(collectionItemId: Long) = either {
        catch({
            collectionItems.getById(collectionItemId)
                .executeAsOneOrNull()
                .toOption()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("Db getById Error", it))
        }
    }

    override fun getOfCollection(collectionId: String) = either {
        catch({
            collectionItems.getOfCollection(collectionId)
                .executeAsList()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("Db getOfCollection Error", it))
        }
    }

    override fun getOfSection(sectionIds: List<String>) = either {
        catch({
            collectionItems.getOfSections(sectionIds = sectionIds)
                .executeAsList()
                .groupBy({ it.sectionId }, { it.toDomainModel() })
        }) {
            raise(DomainError("Db getOfSection Error", it))
        }
    }

    override fun update(collectionItemId: Long, data: CollectionItemMutationData) = either {
        catch({
            data.isSelected.onSome { collectionItems.updateIsSelectedById(it, collectionItemId) }
        }) {
            raise(DomainError("Db update Error", it))
        }
        val result = getById(collectionItemId).bind().fold({ raise(DomainError()) }) { it }
        return@either result
    }
}