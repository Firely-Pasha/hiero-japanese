package space.compoze.hiero.ui.shared.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent

interface MainComponent {

    val state: Value<Model>

    val childStack: Value<ChildStack<*, Child>>

    fun changeTab(index: Int)

    sealed interface Child {
        class Hiragana(val component: StackNavigationComponent) : Child
        class Katakana(val component: StackNavigationComponent) : Child
        class Settings(val component: StackNavigationComponent) : Child
    }

    sealed interface Config : Parcelable {
        @Parcelize
        object Hiragana : Config

        @Parcelize
        object Katakana : Config

        @Parcelize
        object Settings : Config
    }

    data class Model(
        val tab: Int
    )
}