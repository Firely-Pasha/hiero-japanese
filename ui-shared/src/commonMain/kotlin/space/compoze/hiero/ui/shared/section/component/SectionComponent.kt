package space.compoze.hiero.ui.shared.section.component

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.ui.shared.section.state.SectionStore

interface SectionComponent {
    val state: Value<SectionStore.State>
    fun toggleItemSelect(itemId: String)
    fun toggleItemBookmark(itemId: String)
    fun selectAll()
    fun clearAll()
    fun startQuiz()
    fun navigateBack()
    fun navigateToItemDetails()
    fun navigateToSection(section: SectionModel)
    fun toggleItemAndSetSelectMode(itemId: String)
    fun toggleItemBySelect(itemId: String)
}
