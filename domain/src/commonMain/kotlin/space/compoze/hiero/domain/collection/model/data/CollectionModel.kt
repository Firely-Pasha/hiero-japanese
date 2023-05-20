package space.compoze.hiero.domain.collection.model.data

data class CollectionModel(
    val id: String,
    val title: String,
    val cols: Int,
    val itemsCount: Long,
)
