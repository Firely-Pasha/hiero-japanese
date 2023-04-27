package space.compoze.hiero.ui.shared.collection

import com.arkivanov.decompose.value.Value

interface CollectionComponent {
    val state: Value<CollectionState>
    fun addItem()
    fun navigateBack()
    fun navigateToItemDetails()
}