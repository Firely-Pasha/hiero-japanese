package space.compoze.hiero.domain.collectionitem.model

data class CollectionItemModel(
    val id: Long,
    val collectionId: String,
    val type: String,
    val value: String,
    val transcription: String,
    val sort: Long,
)
