package space.compoze.hiero.domain.section.model

data class SectionModel(
    val id: String,
    val collectionId: String,
    val title: String,
    val selectedCount: Long,
    val bookmarkedCount: Long,
    val span: Int,
)