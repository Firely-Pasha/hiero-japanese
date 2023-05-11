package space.compoze.hiero.data.section

import migrations.Section
import space.compoze.hiero.domain.section.model.SectionModel

fun Section.toDomainModel() = SectionModel(
    id = id,
    collectionId = collectionId,
    title = title,
    selectedCount = selectedCount,
    bookmarkedCount = bookmarkedCount,
    span = span.toInt(),
)