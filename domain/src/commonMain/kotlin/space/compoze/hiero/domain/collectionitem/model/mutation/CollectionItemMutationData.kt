package space.compoze.hiero.domain.collectionitem.model.mutation

import arrow.core.Option

data class CollectionItemMutationData(
    val isSelected: Option<Boolean>
)