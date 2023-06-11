package space.compoze.hiero.ui.shared.collection.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
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
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            store.labels bindTo ::onLabel
        }
    }

    private fun onLabel(label: CollectionStore.Label) {
        when (label) {
            is CollectionStore.Label.StartQuiz -> {
                navigateToQuiz(label.items)
            }
        }
    }

    private fun navigateToQuiz(items: List<Long>) {
        navigationComponent.navigation.push(
            StackNavigationComponent.Config.Quiz(
                items = items
            )
        )
    }

    override fun navigateBack() {
        navigationComponent.navigateBack()
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

    override fun startQuiz(isBookmarkOnly: Boolean) {
        store.accept(CollectionStore.Intent.StartQuiz(isBookmarkOnly))
    }
}