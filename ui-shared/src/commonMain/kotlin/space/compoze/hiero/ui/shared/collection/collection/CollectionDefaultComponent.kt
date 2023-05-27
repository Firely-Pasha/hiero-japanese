package space.compoze.hiero.ui.shared.collection.collection

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.domain.section.model.data.SectionModel
import space.compoze.hiero.ui.shared.collection.store.CollectionStore
import space.compoze.hiero.ui.shared.collection.store.CollectionStoreProvider
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent


class CollectionDefaultComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
    private val collectionId: String,
) : CollectionComponent, KoinComponent, ComponentContext by componentContext {

    private val dispatchers: AppDispatchers by inject()
    
    private val store = instanceKeeper.getStore {
        CollectionStoreProvider(storeFactory).create(collectionId)
    }

    override val state = MutableValue(store.state)

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            store.states bindTo { state.value = it }
        }
    }

    override fun addItem() {
        store.accept(CollectionStore.Intent.AddSection)
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