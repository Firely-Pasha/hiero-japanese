package space.compoze.hiero.data.collectionitem

import space.compose.hiero.datasource.database.CollectionItem
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel

fun CollectionItem.toDomainModel(variants: Map<String, String>) = CollectionItemModel(
    id = id,
    sectionId = sectionId,
    type = type,
    isSelected = isSelected,
    isBookmarked = isBookmarked,
    sort = sort,
    variants = variants
)