package space.compoze.hiero.data.collection

import space.compose.hiero.datasource.database.Collection
import space.compoze.hiero.domain.collection.model.data.CollectionModel

fun Collection.toDomainModel() = CollectionModel(
    id = id,
    title = title,
    cols = cols.toInt(),
    itemsCount = itemsCount,
)