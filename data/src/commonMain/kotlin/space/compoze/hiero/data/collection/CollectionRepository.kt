package space.compoze.hiero.data.collection

import app.cash.sqldelight.db.SqlDriver
import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.toOption
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.CollectionRepository
import space.compoze.hiero.domain.collection.model.CollectionModel

class CollectionRepository(
    database: Database
) : CollectionRepository {

    private val collections = database.collectionQueries

    override fun getByUuid(uuid: String) = either {
        catch({
            collections.getByUuid(uuid)
                .executeAsOneOrNull()
                .toOption()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("Db Error", it))
        }
    }


}