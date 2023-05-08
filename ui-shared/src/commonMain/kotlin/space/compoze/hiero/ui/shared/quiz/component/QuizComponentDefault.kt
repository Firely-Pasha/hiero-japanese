package space.compoze.hiero.ui.shared.quiz.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.launch
import space.compoze.hiero.ui.shared.quiz.store.QuizIntent
import space.compoze.hiero.ui.shared.quiz.store.QuizStoreProvider
import space.compoze.hiero.ui.shared.utils.inheritScope

class QuizComponentDefault(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val items: List<Long>,
) : QuizComponent, ComponentContext by componentContext {

    private val componentScope = inheritScope()

    private val store = instanceKeeper.getStore {
        QuizStoreProvider(storeFactory).create(items)
    }

    override val state = MutableValue(store.state)

    init {
        componentScope.launch {
            store.states.collect { state.value = it }
        }
    }

    override fun nextItem() {
        store.accept(QuizIntent.NextItem)
    }

}