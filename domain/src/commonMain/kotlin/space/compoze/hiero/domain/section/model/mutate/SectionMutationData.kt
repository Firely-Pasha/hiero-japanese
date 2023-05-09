package space.compoze.hiero.domain.section.model.mutate

import arrow.core.Option

data class SectionMutationData(
    val selectedCount: Option<Long>
)

sealed interface SectionComputedMutation {
    data class AddSelectedCount(
        val value: Long
    ): SectionComputedMutation
}