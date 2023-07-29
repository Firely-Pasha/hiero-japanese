package space.compoze.hiero.ui.shared.quiz.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.SharedFlow
import space.compoze.hiero.ui.shared.quiz.store.QuizStore

interface QuizComponent {

    val state: Value<QuizStore.State>

    sealed interface Action {
        object NavigateBack : Action
        object NextItem : Action
        object BookmarkCurrentItem : Action
        object SwapVariants : Action
    }

    fun onAction(action: Action)
}