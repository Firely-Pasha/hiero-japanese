package space.compoze.hiero.data.collectionitem

import arrow.core.Option
import arrow.core.firstOrNone
import arrow.core.raise.catch
import arrow.core.raise.either
import space.compose.hiero.datasource.database.CollectionItem
import space.compose.hiero.datasource.database.HieroDb
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collectionitem.CollectionItemRepository
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.collectionitem.model.mutation.CollectionItemMutationData

class CollectionItemRepository(
    database: HieroDb,
) : CollectionItemRepository {

    private val collectionItems = database.collectionItemQueries

    override fun getById(collectionItemId: String) = either {
        getByIds(listOf(collectionItemId)).bind().firstOrNone()
    }

    override fun getByIds(collectionItemIds: List<String>) = either {
        catch({
            collectionItems.getByIds(collectionItemIds)
                .executeAsList()
                .toDomainModel()
        }) {
            raise(DomainError("Db getByIds(${collectionItemIds}) Error", it))
        }
    }

    override fun getOfCollection(collectionId: String) = either {
        catch({
            collectionItems.getOfCollection(collectionId)
                .executeAsList()
                .toDomainModel()
        }) {
            raise(DomainError("Db getOfCollection Error", it))
        }
    }

    override fun getOfSection(sectionIds: List<String>) = either {
        catch({
            val result = collectionItems.getOfSections(sectionIds = sectionIds)
                .executeAsList()
                .groupBy { it.sectionId }
                .mapValues { it.value.toDomainModel() }
            result
        }) {
            raise(DomainError("Db getOfSection Error", it))
        }
    }

    override fun update(collectionItemId: String, data: CollectionItemMutationData) = either {
        catch({
            collectionItems.transaction {
                data.isSelected.onSome {
                    collectionItems.updateIsSelectedById(
                        it,
                        collectionItemId
                    )
                }
                data.isBookmarked.onSome {
                    collectionItems.updateIsBookmarkedById(
                        it,
                        collectionItemId
                    )
                }
            }
        }) {
            raise(DomainError("Db update Error", it))
        }
        return@either getById(collectionItemId).bind<Option<CollectionItemModel>>()
            .fold<CollectionItemModel>({ this.raise(DomainError()) }) { it }
    }

    override fun countOfCollection(collectionIds: List<String>) = either {
        catch({
            collectionItems.countOfCollection(collectionIds).executeAsList()
                .associateBy({ it.collectionId!! }) { it.count }
        }) {
            raise(DomainError("Db update Error", it))
        }
    }

    override fun countOfSection(sectionIds: List<String>) = either {
        catch({
            collectionItems.countOfSection(sectionIds).executeAsList()
                .associateBy({ it.id!! }) { it.count }
        }) {
            raise(DomainError("Db update Error", it))
        }
    }

    private fun List<CollectionItem>.toDomainModel(): List<CollectionItemModel> {
        val collectionItemIds = collectionItems.getVariants(map { it.id })
            .executeAsList()
            .groupBy { it.collectionItemId }
        return map {
            val collectionVariants = collectionItemIds[it.id]
                .orEmpty()
                .associate { it.collectionItemVariant to it.content }
            it.toDomainModel(collectionVariants)
        }
    }
}