package space.compoze.hiero.data.section

import arrow.core.raise.catch
import arrow.core.raise.either
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionRepository(
    private val database: Database
) : SectionRepository {

    override fun getByCollection(collectionId: String) = either {
        catch({
            database.sectionQueries.getOfCollection(collectionId)
                .executeAsList()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("Collection $collectionId error", it))
        }
    }

}