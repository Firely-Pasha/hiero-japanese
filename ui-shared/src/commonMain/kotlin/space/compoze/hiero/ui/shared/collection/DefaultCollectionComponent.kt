package space.compoze.hiero.ui.shared.collection

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.launch
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope


class DefaultCollectionComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
    private val collectionId: String,
) : CollectionComponent, ComponentContext by componentContext {

    private val componentScope = inheritScope()

    private val store = instanceKeeper.getStore {
        CollectionStoreProvider(storeFactory).create(componentScope, collectionId)
    }

    override val state = MutableValue(store.state)

    init {
        componentScope.launch {
            store.states.collect { state.value = it }
        }
    }

    override fun addItem() {
        store.accept(CollectionIntent.AddSection)
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
                sectionId = section.id,
                collectionId = if (section == CollectionComponent.AllSection) {
                    collectionId
                } else {
                    null
                },
            )
        )
    }
}