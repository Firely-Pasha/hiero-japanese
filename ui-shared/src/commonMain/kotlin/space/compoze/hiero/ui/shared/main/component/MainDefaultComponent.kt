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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import space.compoze.hiero.ui.shared.main.store.MainStore
import space.compoze.hiero.ui.shared.main.store.MainStoreProvider
import space.compoze.hiero.ui.shared.main.toModel
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope

class MainDefaultComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    appNavigator: StackNavigationComponent,
) : MainComponent, ComponentContext by componentContext {

    private val scope = inheritScope()

    private val store = instanceKeeper.getStore {
        MainStoreProvider(storeFactory).create()
    }

    override val state = MutableValue(store.state.toModel())

    private val navigation = StackNavigation<MainComponent.Config>()

    override val childStack = childStack(
        source = navigation,
        initialStack = {
            listOf(
                MainComponent.Config.Settings,
                MainComponent.Config.Katakana,
                MainComponent.Config.Hiragana,
            )
        },
        key = "BottomStack",
//        initialConfiguration = store.state.tab.toConfig(),
        handleBackButton = true, // Pop the back stack on back button press
        childFactory = { config, componentContext ->
            when (config) {
                is MainComponent.Config.Hiragana -> MainComponent.Child.Hiragana(
                    space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent(
                        componentContext,
                        storeFactory,
                        StackNavigationComponent.Config.Hiragana,
                        appNavigator = appNavigator,
                    )
                )

                is MainComponent.Config.Katakana -> MainComponent.Child.Katakana(
                    space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent(
                        componentContext,
                        storeFactory,
                        StackNavigationComponent.Config.Katakana,
                        appNavigator = appNavigator,
                    )
                )

                is MainComponent.Config.Settings -> MainComponent.Child.Settings(
                    space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent(
                        componentContext,
                        storeFactory,
                        StackNavigationComponent.Config.Settings,
                        appNavigator = appNavigator,
                    )
                )
            }
        },
    )

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, Dispatchers.Unconfined) {
            store.states bindTo ::onStateChange
        }
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, Dispatchers.Unconfined) {
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
        scope.launch {
            withContext(Dispatchers.Main) {
                store.accept(MainStore.Intent.ChangeTab(index))
            }
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