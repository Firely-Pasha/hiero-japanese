package space.compoze.hiero.ui.shared.stacknavigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import space.compoze.hiero.ui.shared.collection.CollectionComponent
import space.compoze.hiero.ui.shared.settings.SettingsComponent

interface StackNavigationComponent {

    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<*, Child>>

    fun navigateBack()

    sealed interface Child {
        class Hiragana(val component: CollectionComponent) : Child
        class Katakana(val component: CollectionComponent) : Child
        class Settings(val component: SettingsComponent) : Child
    }

    sealed interface Config : Parcelable {
        @Parcelize
        object Hiragana : Config

        @Parcelize
        object Katakana : Config

        @Parcelize
        object Settings : Config
    }
}