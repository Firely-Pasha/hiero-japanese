package space.compoze.hiero.ui.shared.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.mp.KoinPlatformTools
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.ui.shared.collection.component.CollectionComponent
import space.compoze.hiero.ui.shared.collection.component.CollectionDefaultComponent
import space.compoze.hiero.ui.shared.main.store.MainStore
import space.compoze.hiero.ui.shared.main.store.MainStoreProvider
import space.compoze.hiero.ui.shared.main.toModel
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope
import kotlin.native.concurrent.ThreadLocal

class MainDefaultComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    appNavigator: StackNavigationComponent,
) : MainComponent, KoinComponent, ComponentContext by componentContext {

    private val scope = inheritScope()

    private val dispatchers: AppDispatchers by inject()

    private val store = instanceKeeper.getStore {
        MainStoreProvider(storeFactory).create()
    }

    override val state = MutableValue(store.state.toModel())

    override val hiraganaTab = MainComponent.Child.Hiragana(
        space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent(
            componentContext,
            storeFactory = storeFactory,
            StackNavigationComponent.Config.Hiragana,
            appNavigator = appNavigator,
        )
    )

    override val katakanaTab = MainComponent.Child.Katakana(
        space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent(
            componentContext,
            storeFactory = storeFactory,
            StackNavigationComponent.Config.Katakana,
            appNavigator = appNavigator,
        )
    )

    override val settingsTab = MainComponent.Child.Settings(
        space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent(
            componentContext,
            storeFactory = storeFactory,
            StackNavigationComponent.Config.Settings,
            appNavigator = appNavigator,
        )
    )

    private val navigation = StackNavigation<MainComponent.Config>()

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            store.states bindTo ::onStateChange
        }
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            store.labels bindTo ::onLabel
        }
    }

    private fun onStateChange(newState: MainStore.State) {
        state.value = newState.toModel()
    }

    private fun onLabel(label: MainStore.Label) {
        when (label) {
            is MainStore.Label.ChangeTab -> {
                navigation.bringToFront(label.value.toConfig())
            }
        }
    }

    override fun changeTab(index: Int) {
        scope.launch(dispatchers.main) {
            store.accept(MainStore.Intent.ChangeTab(index))
        }
    }
}

private fun Int.toConfig(): MainComponent.Config {
    return when (this) {
        0 -> MainComponent.Config.Hiragana
        1 -> MainComponent.Config.Katakana
        2 -> MainComponent.Config.Settings
        else -> TODO()
    }
}