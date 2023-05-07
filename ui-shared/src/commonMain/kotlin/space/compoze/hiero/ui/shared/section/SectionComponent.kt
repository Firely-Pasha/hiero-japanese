package space.compoze.hiero.ui.shared.section

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.domain.section.model.SectionModel

interface SectionComponent {
    val state: Value<SectionState>
    fun toggleItemSelect(itemId: Long)
    fun navigateBack()
    fun navigateToItemDetails()
    fun navigateToSection(section: SectionModel)
}