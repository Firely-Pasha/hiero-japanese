package space.compoze.hiero.domain.collectionitem.model.data

data class CollectionItemModel(
    val id: String,
    val sectionId: String,
    val type: String,
    val isSelected: Boolean,
    val isBookmarked: Boolean,
    val sort: Long,
    val variants: Map<String, String>,
)
