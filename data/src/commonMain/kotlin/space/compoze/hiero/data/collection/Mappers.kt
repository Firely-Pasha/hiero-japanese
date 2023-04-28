package space.compoze.hiero.data.collection

import migrations.Collection
import space.compoze.hiero.domain.collection.model.CollectionModel

fun Collection.toDomainModel() = CollectionModel(
    uuid = uuid,
    title = title,
)