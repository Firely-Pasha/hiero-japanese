package space.compoze.hiero.domain.section.model.mutate

sealed interface SectionComputedMutation {
    data class AddSelectedCount(
        val value: Long
    ): SectionComputedMutation
    data class AddBookmarkedCount(
        val value: Long
    ): SectionComputedMutation
}