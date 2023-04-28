package space.compoze.hiero.ui.shared.stacknavigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.mvikotlin.core.store.StoreFactory
import space.compoze.hiero.ui.shared.collection.DefaultCollectionComponent
import space.compoze.hiero.ui.shared.settings.DefaultSettingsComponent

class DefaultStackNavigationComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    startConfig: StackNavigationComponent.Config
) : StackNavigationComponent, ComponentContext by componentContext {

    override val navigation = StackNavigation<StackNavigationComponent.Config>()

    override val childStack = childStack(
        source = navigation,
        key = "StackNavigation$startConfig",
        initialConfiguration = startConfig,
        handleBackButton = true, // Pop the back stack on back button press
        childFactory = { config, componentContext ->
            when (config) {
                is StackNavigationComponent.Config.Hiragana -> StackNavigationComponent.Child.Hiragana(
                    DefaultCollectionComponent(
                        componentContext,
                        storeFactory,
                        this,
                        "hiragana",
                    )
                )

                is StackNavigationComponent.Config.Katakana -> StackNavigationComponent.Child.Katakana(
                    DefaultCollectionComponent(
                        componentContext,
                        storeFactory,
                        this,
                        "2"
                    )
                )

                is StackNavigationComponent.Config.Settings -> StackNavigationComponent.Child.Settings(
                    DefaultSettingsComponent(
                        componentContext,
                        storeFactory,
                        this,
                    )
                )
            }
        },
    )

    override fun navigateBack() {
        navigation.pop()
    }
}