package space.compoze.hiero.domain.collection.model.mutation

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation

sealed class CollectionMutation(
    val itemsCount: Option<Long> = None,
) {

    data class UpdateItemsCount(
        val value: Long,
    ) : CollectionMutation(
        itemsCount = value.toOption()
    )

}