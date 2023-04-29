package space.compoze.hiero.data.collectionitem

import migrations.CollectionItem
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel

fun CollectionItem.toDomainModel() = CollectionItemModel(
    id = id,
    sectionId = sectionId,
    type = type,
    value = value_,
    transcription = transcription,
    sort = sort,
)