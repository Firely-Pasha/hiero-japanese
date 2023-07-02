package space.compoze.hiero.data.collectionitem

import space.compose.hiero.datasource.database.CollectionItem
import space.compose.hiero.datasource.database.CollectionItemVariant
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemVariantModel

fun CollectionItem.toDomainModel(variants: Map<String, String>) = CollectionItemModel(
    id = id,
    sectionId = sectionId,
    type = type,
    isSelected = isSelected,
    isBookmarked = isBookmarked,
    sort = sort,
    variants = variants
)

fun CollectionItemVariant.toDomainModel() = CollectionItemVariantModel(
    id = id,
    collectionId = collectionId,
    name = name,
    type = type,
)