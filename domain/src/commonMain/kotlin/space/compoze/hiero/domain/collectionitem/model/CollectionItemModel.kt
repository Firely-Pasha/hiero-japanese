package space.compoze.hiero.domain.collectionitem.model

data class CollectionItemModel(
    val id: Long,
    val sectionId: String,
    val type: String,
    val value: String,
    val transcription: String,
    val sort: Long,
    val isSelected: Boolean = false
)
