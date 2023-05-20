package space.compoze.hiero.domain.section.model.mutate

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption

sealed class SectionMutation(
    val itemsCount: Option<Long> = None,
) {
    data class UpdateItemsCount(
        val value: Long,
    ) : SectionMutation(
        itemsCount = value.toOption()
    )
}