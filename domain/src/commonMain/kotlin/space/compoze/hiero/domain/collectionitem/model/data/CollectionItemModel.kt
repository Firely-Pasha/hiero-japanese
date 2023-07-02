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

data class CollectionItemVariantModel(
    val id: String,
    val collectionId: String,
    val name: String,
    val type: Long?,
)

fun CollectionItemVariantModel.isPrimary() = type == 1L
fun CollectionItemVariantModel.isSecondary() = type == 2L
