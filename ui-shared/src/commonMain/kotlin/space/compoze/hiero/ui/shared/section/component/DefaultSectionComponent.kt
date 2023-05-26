package space.compoze.hiero.ui.shared.section.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.ui.shared.section.state.SectionStore
import space.compoze.hiero.ui.shared.section.state.SectionStoreProvider
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope
import space.compoze.hiero.ui.shared.utils.with


class DefaultSectionComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
    private val sectionId: String,
    private val collectionId: String? = null,
) : SectionComponent, ComponentContext by componentContext {

    private val componentScope = inheritScope()

    private val store = instanceKeeper.getStore {
        SectionStoreProvider(storeFactory).create(componentScope, sectionId, collectionId)
    }

    override val state = MutableValue(store.state)

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, Dispatchers.Unconfined) {
            store.states bindTo ::onStateChange
        }
    }

    private fun onStateChange(newState: SectionStore.State) {
        state.value = newState
    }

    override fun toggleItemSelect(itemId: Long) {
        store.accept(SectionStore.Intent.ToggleItemSelect(itemId = itemId))
    }

    override fun toggleItemBookmark(itemId: Long) {
        store.accept(SectionStore.Intent.ToggleItemBookmark(itemId = itemId))
    }

    override fun selectAll() {
        store.accept(SectionStore.Intent.SelectAll)
    }

    override fun clearAll() {
        store.accept(SectionStore.Intent.ClearAll)
    }

    override fun startQuiz() {
        state.value.with { content: SectionStore.State.Content ->
            navigationComponent.navigation.push(
                StackNavigationComponent.Config.Quiz(
                    content.items.filter { it.isSelected && it.type != "empty" }.map { it.id }
                )
            )
        }
    }

    override fun navigateBack() {
        navigationComponent.navigateBack()
    }

    override fun navigateToItemDetails() {
        navigationComponent.navigation.push(StackNavigationComponent.Config.Katakana)
    }

    override fun navigateToSection(section: SectionModel) {
        navigationComponent.navigation.push(
            StackNavigationComponent.Config.Section(
                sectionId = section.id
            )
        )
    }
}