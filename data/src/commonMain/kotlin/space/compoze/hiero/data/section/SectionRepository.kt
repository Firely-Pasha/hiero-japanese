package space.compoze.hiero.data.section

import app.cash.sqldelight.coroutines.asFlow
import arrow.core.Either
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.singleOrNone
import arrow.core.toOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import space.compose.hiero.datasource.database.HieroDb
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation
import space.compoze.hiero.domain.section.model.mutate.SectionMutation
import space.compoze.hiero.domain.section.repository.SectionRepository

class SectionRepository(
    private val database: HieroDb,
) : SectionRepository {

    override fun getById(sectionId: String) = either {
        catch({
            database.sectionQueries.getById(sectionId)
                .executeAsOneOrNull()
                ?.toDomainModel()
                .toOption()
        }) {
            raise(DomainError("SectionRepository::getById(sectionId: $sectionId) error", it))
        }
    }

    override fun getByIds(sectionIds: List<String>): Either<DomainError, Map<String, Option<SectionModel>>> = either {
        catch({
            database.sectionQueries.getByIds(sectionIds)
                .executeAsList()
                .groupBy { it.id }
                .let { sections ->
                    sectionIds.associateWith {
                        sections[it].orEmpty().singleOrNone().map { it.toDomainModel() }
                    }
                }
        }) {
            raise(DomainError("SectionRepository::getById(sectionIds: ${sectionIds.joinToString()}) error", it))
        }
    }

    override fun getAll() = either {
        catch({
            database.sectionQueries.getAll()
                .executeAsList()
                .map { it.toDomainModel() }
        }) {
            raise(DomainError("SectionRepository::getAll() error", it))
        }
    }

    override fun getByCollection(collectionId: String) = either {
        catch({
            database.sectionQueries.getOfCollection(collectionId)
                .executeAsList()
                .map { it.toDomainModel() }
        }) {
            raise(
                DomainError(
                    "SectionRepository::getByCollection(collectionId: $collectionId) error",
                    it
                )
            )
        }
    }

    override fun flowByCollection(collectionId: String): Flow<List<SectionModel>> {
        return database.sectionQueries.getOfCollection(collectionId)
            .asFlow()
            .map { it.executeAsList().map { it.toDomainModel() } }
    }

    override fun update(sectionId: String, data: SectionMutation) = either {
        catch({
            database.sectionQueries.transaction {
                data.itemsCount.onSome {
                    database.sectionQueries.updateItemsCountById(it, sectionId)
                }
            }
        }) {
            raise(
                DomainError(
                    "SectionRepository::update(collectionId: $sectionId, data: $data) error",
                    it
                )
            )
        }
        getById(sectionId).bind().fold({ this.raise(DomainError()) }) { it }
    }


    override fun updateComputed(sectionId: String, data: SectionComputedMutation) = either {
        catch({
            database.sectionQueries.transaction {
                when (data) {
                    is SectionComputedMutation.AddSelectedCount -> database.sectionQueries.addSelectedCount(
                        data.value, sectionId
                    )

                    is SectionComputedMutation.AddBookmarkedCount -> database.sectionQueries.addBookmarkedCount(
                        data.value, sectionId
                    )
                }
            }
        }) {
            raise(
                DomainError(
                    "SectionRepository::updateComputed(sectionId: $sectionId, data: $data) error",
                    it
                )
            )
        }
        getById(sectionId).bind().getOrElse { throw DomainError() }
    }

}