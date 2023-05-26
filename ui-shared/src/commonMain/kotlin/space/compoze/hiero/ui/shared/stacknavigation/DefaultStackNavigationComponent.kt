package space.compoze.hiero.ui.shared.stacknavigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.mvikotlin.core.store.StoreFactory
import space.compoze.hiero.ui.shared.collection.collection.CollectionDefaultComponent
import space.compoze.hiero.ui.shared.main.component.MainDefaultComponent
import space.compoze.hiero.ui.shared.quiz.component.QuizComponentDefault
import space.compoze.hiero.ui.shared.section.component.DefaultSectionComponent
import space.compoze.hiero.ui.shared.settings.component.DefaultSettingsComponent

class DefaultStackNavigationComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    startConfig: StackNavigationComponent.Config,
    appNavigator: StackNavigationComponent? = null
) : StackNavigationComponent, ComponentContext by componentContext {

    private val appNavigator = appNavigator ?: this

    override val navigation = StackNavigation<StackNavigationComponent.Config>()

    override val childStack = childStack(
        source = navigation,
        key = "StackNavigation$startConfig",
        initialConfiguration = startConfig,
        handleBackButton = true, // Pop the back stack on back button press
        childFactory = { config, componentContext ->
            when (config) {
                is StackNavigationComponent.Config.Hiragana -> StackNavigationComponent.Child.Collection(
                    CollectionDefaultComponent(
                        componentContext,
                        storeFactory,
                        this.appNavigator,
                        "hiragana",
                    )
                )

                is StackNavigationComponent.Config.Katakana -> StackNavigationComponent.Child.Collection(
                    CollectionDefaultComponent(
                        componentContext,
                        storeFactory,
                        this.appNavigator,
                        "katakana"
                    )
                )
                is StackNavigationComponent.Config.Section -> StackNavigationComponent.Child.Section(
                    DefaultSectionComponent(
                        componentContext,
                        storeFactory,
                        this.appNavigator,
                        sectionId = config.sectionId,
                        collectionId = config.collectionId
                    )
                )
                is StackNavigationComponent.Config.Settings -> StackNavigationComponent.Child.Settings(
                    DefaultSettingsComponent(
                        componentContext,
                        storeFactory,
                        this.appNavigator,
                    )
                )

                is StackNavigationComponent.Config.Quiz -> StackNavigationComponent.Child.Quiz(
                    QuizComponentDefault(
                        componentContext,
                        storeFactory,
                        this.appNavigator,
                        config.items
                    )
                )

                StackNavigationComponent.Config.Main -> StackNavigationComponent.Child.Main(
                    MainDefaultComponent(
                        componentContext,
                        storeFactory,
                        this.appNavigator,
                    )
                )
            }
        },
    )

    override fun navigateBack() {
        navigation.pop()
    }
}