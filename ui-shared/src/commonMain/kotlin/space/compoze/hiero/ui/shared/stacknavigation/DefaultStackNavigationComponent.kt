package space.compoze.hiero.ui.shared.stacknavigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.mvikotlin.core.store.StoreFactory
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.mp.KoinPlatformTools
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.ui.shared.collection.component.CollectionDefaultComponent
import space.compoze.hiero.ui.shared.main.component.MainDefaultComponent
import space.compoze.hiero.ui.shared.quiz.component.QuizComponentDefault
import space.compoze.hiero.ui.shared.section.component.DefaultSectionComponent
import space.compoze.hiero.ui.shared.settings.component.DefaultSettingsComponent

private const val HIRAGANA_COLLECTION_ID = "38ddff20-2211-4073-a5f8-b13cc572e473"
private const val KATAKANA_COLLECTION_ID = "b4741f39-9fac-4189-b8d7-24b4931db9b3"

class DefaultStackNavigationComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    startConfig: StackNavigationComponent.Config,
    appNavigator: StackNavigationComponent? = null
) : StackNavigationComponent, KoinComponent, ComponentContext by componentContext {

    private val dispatchers: AppDispatchers by inject()

    override val isRoot = appNavigator == null

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
                        HIRAGANA_COLLECTION_ID,
                    )
                )

                is StackNavigationComponent.Config.Katakana -> StackNavigationComponent.Child.Collection(
                    CollectionDefaultComponent(
                        componentContext,
                        storeFactory,
                        this.appNavigator,
                        KATAKANA_COLLECTION_ID
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