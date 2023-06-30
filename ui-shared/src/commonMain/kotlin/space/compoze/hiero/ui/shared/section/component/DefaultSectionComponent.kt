package space.compoze.hiero.ui.shared.section.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.ui.shared.section.state.SectionStore
import space.compoze.hiero.ui.shared.section.state.SectionStoreProvider
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.with


class DefaultSectionComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
    private val sectionId: String,
    private val collectionId: String? = null,
) : SectionComponent, KoinComponent, ComponentContext by componentContext {

    private val dispatchers: AppDispatchers by inject()

    private val store = instanceKeeper.getStore {
        SectionStoreProvider(storeFactory).create(sectionId, collectionId)
    }

    override val state = MutableValue(store.state)

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            store.states bindTo ::onStateChange
        }
    }

    private fun onStateChange(newState: SectionStore.State) {
        state.value = newState
    }

    override fun toggleItemSelect(itemId: String) {
        store.accept(SectionStore.Intent.ToggleItemSelect(itemId = itemId))
    }

    override fun toggleItemAndSetSelectMode(itemId: String) {
        store.accept(SectionStore.Intent.ToggleItemAndSetSelectMode(itemId = itemId))
    }

    override fun toggleItemBySelect(itemId: String) {
        store.accept(SectionStore.Intent.ToggleItemBySelect(itemId = itemId))
    }

    override fun toggleItemBookmark(itemId: String) {
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
                    content.items
                        .filter { it.type != "empty" }
                        .filter { it.isSelected || it.isBookmarked }
                        .map { it.id }
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
