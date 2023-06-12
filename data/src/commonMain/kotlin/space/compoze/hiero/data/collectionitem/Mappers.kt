package space.compoze.hiero.data.collectionitem

import space.compose.hiero.datasource.database.CollectionItem
import space.compoze.hiero.domain.collectionitem.model.data.CollectionItemModel

fun CollectionItem.toDomainModel() = CollectionItemModel(
    id = id,
    sectionId = sectionId,
    type = type,
    value = value_,
    transcription = transcription,
    isSelected = isSelected,
    isBookmarked = isBookmarked,
    sort = sort,
)