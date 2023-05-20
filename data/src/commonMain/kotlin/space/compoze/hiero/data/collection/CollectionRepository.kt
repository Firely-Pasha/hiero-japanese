package space.compoze.hiero.data.collection

import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.toOption
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.CollectionRepository
import space.compoze.hiero.domain.collection.model.mutation.CollectionMutation

class CollectionRepository(
    database: Database,
) : CollectionRepository {

    private val collections = database.collectionQueries

    override fun getById(uuid: String) = either {
        catch({
            collections.getByUuid(uuid)
                .executeAsOneOrNull()
                .toOption()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("CollectionRepository::getById(uuid: $uuid) error", it))
        }
    }

    override fun getAll() = either {
        catch({
            collections.getAll()
                .executeAsList()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("CollectionRepository::getAll() error", it))
        }
    }

    override fun update(collectionId: String, data: CollectionMutation) = either {
        catch({
            collections.transaction {
                data.itemsCount.onSome {
                    collections.updateItemsCountById(it, collectionId)
                }
            }
        }) {
            raise(
                DomainError(
                    "CollectionRepository::update(collectionId: $collectionId, data: $data) error",
                    it
                )
            )
        }
        return@either getById(collectionId).bind()
            .fold({ this.raise(DomainError()) }) { it }
    }

}