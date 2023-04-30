package space.compoze.hiero.ui.shared.section

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.launch
import space.compoze.hiero.domain.section.model.SectionModel
import space.compoze.hiero.ui.shared.collection.CollectionComponent
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope


class DefaultSectionComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
    private val sectionId: String,
    private val collectionId: String? = null,
) : SectionComponent, ComponentContext by componentContext {

    private val componentScope = inheritScope()

    private val store = instanceKeeper.getStore {
        SectionStoreProvider(storeFactory).create(sectionId, collectionId)
    }

    override val state = MutableValue(store.state)

    init {
        componentScope.launch {
            store.states.collect { state.value = it }
        }
    }

    override fun addItem() {
        store.accept(SectionIntent.AddSection)
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