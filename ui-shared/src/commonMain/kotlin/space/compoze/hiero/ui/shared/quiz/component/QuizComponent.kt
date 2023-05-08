package space.compoze.hiero.ui.shared.quiz.component

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.ui.shared.quiz.store.QuizState

interface QuizComponent {

    val state: Value<QuizState>

    fun navigateBack()
    fun nextItem()
}