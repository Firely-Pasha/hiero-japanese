package space.compoze.hiero.ui.shared.collection.component

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.ui.shared.collection.store.CollectionStore

interface CollectionComponent {
    val state: Value<CollectionStore.State>
    fun navigateBack()
    fun navigateToSection(sectionId: String)
    fun startQuiz(isBookmarkOnly: Boolean)

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