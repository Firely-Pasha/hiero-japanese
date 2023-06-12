package space.compoze.hiero.ui.shared.stacknavigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import space.compoze.hiero.ui.shared.collection.component.CollectionComponent
import space.compoze.hiero.ui.shared.main.component.MainComponent
import space.compoze.hiero.ui.shared.quiz.component.QuizComponent
import space.compoze.hiero.ui.shared.section.component.SectionComponent
import space.compoze.hiero.ui.shared.settings.component.SettingsComponent

interface StackNavigationComponent {

    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<*, Child>>

    val isRoot: Boolean

    fun navigateBack()

    sealed interface Child {
        class Collection(val component: CollectionComponent) : Child
        class Section(val component: SectionComponent) : Child
        class Settings(val component: SettingsComponent) : Child
        class Quiz(val component: QuizComponent) : Child
        class Main(val component: MainComponent) : Child
    }

    sealed interface Config : Parcelable {
        @Parcelize
        object Hiragana : Config
        @Parcelize
        data class Section(
            val sectionId: String,
            val collectionId: String? = null,
        ) : Config

        @Parcelize
        object Katakana : Config

        @Parcelize
        object Settings : Config

        @Parcelize
        data class Quiz(
            val items: List<Long>
        ) : Config

        @Parcelize
        object Main : Config
    }
}