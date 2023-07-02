package space.compoze.hiero.ui.shared.quiz.component

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.ui.shared.quiz.store.QuizStore

interface QuizComponent {

    val state: Value<QuizStore.State>

    fun navigateBack()
    fun nextItem()
    fun bookmarkCurrentItem()
    fun swapVariants()
}