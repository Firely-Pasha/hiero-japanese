package space.compoze.hiero.data.collectionitem

import migrations.Collection
import migrations.CollectionItem
import space.compoze.hiero.domain.collection.model.CollectionModel
import space.compoze.hiero.domain.collectionitem.model.CollectionItemModel

fun CollectionItem.toDomainModel() = CollectionItemModel(
    id = id,
    collectionId = collectionId,
    type = type,
    value = value_,
    transcription = transcription,
    sort = sort,
)