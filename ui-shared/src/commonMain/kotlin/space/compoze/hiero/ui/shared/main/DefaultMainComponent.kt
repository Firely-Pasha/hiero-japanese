package space.compoze.hiero.ui.shared.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.launch
import space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope

class DefaultMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
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
                    DefaultStackNavigationComponent(
                        componentContext,
                        storeFactory,
//                        StackNavigationComponent.Config.Section(collectionId = "hiragana", sectionId = "%ALL%"),
                        StackNavigationComponent.Config.Hiragana,
                    )
                )

                is MainComponent.Config.Katakana -> MainComponent.Child.Katakana(
                    DefaultStackNavigationComponent(
                        componentContext,
                        storeFactory,
                        StackNavigationComponent.Config.Katakana,
                    )
                )

                is MainComponent.Config.Settings -> MainComponent.Child.Settings(
                    DefaultStackNavigationComponent(
                        componentContext,
                        storeFactory,
                        StackNavigationComponent.Config.Settings,
                    )
                )
            }
        },
    )

    init {
        scope.launch {
            store.states.collect {
                state.value = it.toModel()
            }
        }
        scope.launch {
            store.labels.collect {
                when (it) {
                    is MainLabel.ChangeTab -> {
                        navigation.bringToFront(it.value.toConfig())
                    }
                }
            }
        }
    }

    override fun changeTab(index: Int) {
        isAssertOnMainThreadEnabled = false
        scope.launch {
            store.accept(MainIntent.ChangeTab(index))
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