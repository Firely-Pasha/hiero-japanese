package space.compoze.hiero.data.section

import migrations.CollectionItem
import migrations.Section
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel
import space.compoze.hiero.domain.section.model.SectionModel

fun Section.toDomainModel() = SectionModel(
    id = id,
    collectionId = collectionId,
    title = title,
)