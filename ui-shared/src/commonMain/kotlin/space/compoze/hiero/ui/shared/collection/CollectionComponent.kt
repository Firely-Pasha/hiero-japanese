package space.compoze.hiero.ui.shared.collection

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.domain.section.model.data.SectionModel

interface CollectionComponent {
    val state: Value<CollectionState>
    fun addItem()
    fun navigateBack()
    fun navigateToItemDetails()
    fun navigateToSection(section: SectionModel)

    companion object {
        val AllSection = SectionModel(
            id = "%ALL%",
            title = "All",
            collectionId = "%DYNAMIC%",
            selectedCount = 0,
            bookmarkedCount = 0,
            span = 15,
            itemsCount = 1
        )
    }
}