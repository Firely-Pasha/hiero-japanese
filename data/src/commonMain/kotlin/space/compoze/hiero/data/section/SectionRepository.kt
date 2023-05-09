package space.compoze.hiero.data.section

import arrow.core.Either
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.toOption
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.section.model.SectionModel
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation
import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionRepository(
    private val database: Database,
) : SectionRepository {

    override fun getById(sectionId: String) = either {
        catch({
            database.sectionQueries.getById(sectionId)
                .executeAsOneOrNull()
                ?.toDomainModel()
                .toOption()
        }) {
            raise(DomainError("Section::getById(sectionId: $sectionId) error", it))
        }
    }

    override fun getByCollection(collectionId: String) = either {
        catch({
            database.sectionQueries.getOfCollection(collectionId)
                .executeAsList()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("Section::getByCollection(collectionId: $collectionId) error", it))
        }
    }

    override fun updateComputed(sectionId: String, data: SectionComputedMutation) = either {
        catch({
            database.sectionQueries.transaction {
                when (data) {
                    is SectionComputedMutation.AddSelectedCount -> database.sectionQueries.updateSelectedCount(
                        data.value, sectionId
                    )
                }
            }
        }) {
            raise(
                DomainError(
                    "Section::updateComputed(sectionId: $sectionId, data: $data) error",
                    it
                )
            )
        }
        getById(sectionId).bind().getOrElse { throw DomainError() }
    }

}